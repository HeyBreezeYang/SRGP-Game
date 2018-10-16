#!/bin/bash
VIP=192.168.10.200
RIP1=192.168.10.59
case "$1" in
    start)
    echo " start LVS "
    # set the Virtual IP Address
    modprobe ipip
    ifconfig tunl0 $VIP broadcast $VIP netmask 255.255.255.255 up
    echo "0" >/proc/sys/net/ipv4/ip_forward
    echo "1" >/proc/sys/net/ipv4/conf/all/send_redirects
    echo "1" >/proc/sys/net/ipv4/conf/default/send_redirects
    echo "1" >/proc/sys/net/ipv4/conf/tunl0/send_redirects
    # route add -host $VIP dev tunl0
    # ipvsadm -C
    # ipvsadm -A -t $VIP:80 -s wlc
    # ipvsadm -a -t $VIP:80 -r $RIP1:80 -i -w 2
    # ipvsadm -a -t $VIP:80 -r $RIP1:80 -i -w 2
    ;;
    stop)
    echo "close LVS Director"
    # ipvsadm -C
    ifconfig tunl0 down
    modprobe -r ipip
    ;;
    *)
    echo "Usage: $0 {start|stop}"
    exit 1
esac