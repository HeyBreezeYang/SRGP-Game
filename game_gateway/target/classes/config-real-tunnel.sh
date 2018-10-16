#!/bin/bash
# Virtual IP
vip=192.168.10.200
# 网关要和虚ip一样，掩码不能同于eth0的
# ifconfig tunl0 $vip broadcast $vip netmask 255.255.255.255 up
# 添加永久路由
# route add -host $vip dev tunl0
# echo "0" >/proc/sys/net/ipv4/ip_forward #关闭real server ip转发，2.6.*内核默认是关闭的
# echo "1" >/proc/sys/net/ipv4/conf/lo/arp_ignore
# echo "2" >/proc/sys/net/ipv4/conf/lo/arp_announce
# echo "1" >/proc/sys/net/ipv4/conf/all/arp_ignore
# echo "2" >/proc/sys/net/ipv4/conf/all/arp_announce
# 查看sysctl的配置变化，可不执行
# sysctl -p
case $1 in
        start)
        echo "tunl0 port starting"
        iptables --flush
        modprobe ipip
        # 为了相应lvs调度器转发过来的包,需在本地lo接口上绑定vip
        ifconfig tunl0 $vip broadcast $vip netmask 255.255.255.255 up
        # route add -host $vip dev tunl0
        # 限制arp请求
        # echo "0" > /proc/sys/net/ipv4/ip_forward
        echo "1" > /proc/sys/net/ipv4/conf/tunl0/arp_ignore
        echo "2" > /proc/sys/net/ipv4/conf/tunl0/arp_announce
        echo "1" > /proc/sys/net/ipv4/conf/all/arp_ignore
        echo "2" > /proc/sys/net/ipv4/conf/all/arp_announce
        echo "0" > /proc/sys/net/ipv4/conf/tunl0/rp_filter
        echo "0" > /proc/sys/net/ipv4/conf/all/rp_filter
        sysctl -p > /dev/null 2>&1
        ;;
        stop)
        echo "tunl0 port closing"
        ifconfig tunl0 down
        modprobe -r ipip
        echo "0" > /proc/sys/net/ipv4/conf/tunl0/arp_ignore
        echo "0" > /proc/sys/net/ipv4/conf/tunl0/arp_announce
        echo "0" > /proc/sys/net/ipv4/conf/all/arp_ignore
        echo "0" > /proc/sys/net/ipv4/conf/all/arp_announce
        echo "0" > /proc/sys/net/ipv4/conf/tunl0/rp_filter
        echo "0" > /proc/sys/net/ipv4/conf/all/rp_filter
        ;;
        *)
        echo "Usage: $0 {start ¦ stop}"
        exit 1
esac