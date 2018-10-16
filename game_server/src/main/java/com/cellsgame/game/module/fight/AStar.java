package com.cellsgame.game.module.fight;

import com.cellsgame.common.util.GameUtil;

import java.util.List;

import com.google.common.collect.HashBasedTable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class Vector2 {
    public float x;
    public float y;

    Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static double distance(Vector2 pos1,Vector2 pos2) {
        return Math.sqrt(Math.pow(pos1.x - pos2.x, 2) +  Math.pow(pos1.y - pos2.y, 2));
    }
}

final class Vector3 {
    public float x;
    public float y;
    public float z;

    Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}

final class Node {
    public Vector2 pos;
    public AStar.NodeType nodeType;
    public AStar.AStarState aStarState;
    public Node[] neighbourNodes;
    public Node parentNode = null;
    public float F = 0;
    public float G = 0;
    public float H = 0;
    //public bool isMoveRoad = false;

    public void init()
    {
        float F = 0;
        float G = 0;
        float H = 0;
        parentNode = null;
        aStarState = AStar.AStarState.free;
        if (nodeType == AStar.NodeType.aStarPath )
            nodeType = AStar.NodeType.movable;

        //isMoveRoad = false;
    }
}

public class AStar  {
    private static final Logger log = LoggerFactory.getLogger(AStar.class);
    //地图节点类型
    public enum NodeType
    {
        boundary,   //边界
        movable,    //可移动
        block,      //障碍物
//        forest,     //森林
//        flying,     //飞行单位才行
//        restore,    //
//        destructible,
//        house,      //
//        warp_portal,//传送阵
        aStarPath,  //A星路径
    }

    public enum AStarState
    {
        free,
        isInOpenList,
        isInCloseList,
    }

    //寻路相关
    private List<Node> openList;
    private List<Node> closeList;
    private Vector2 globalEndPos;


    private HashBasedTable<Integer, Integer, Node> posXYNodeDict;

    public AStar() {
        openList = GameUtil.createList();
        closeList = GameUtil.createList();
    }
//    private Map<Vector2, Node> posNodeDict;

    /// <summary>
    /// 初始化节点数据
    /// </summary>
    /// <param name="mapHeight"></param>
    /// <param name="mapLength"></param>
    /// <returns></returns>
    public HashBasedTable initMap(int mapHeight, int mapLength) {
        HashBasedTable nodes = HashBasedTable.create(mapHeight, mapLength);
        posXYNodeDict = HashBasedTable.create();
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapLength; j++) {
//                nodes[i, j] = new Node();
                Node node = new Node();
                nodes.put(i, j, node);
                //边界判断
                if (i == 0) {
                    node.nodeType = NodeType.boundary;
//                    node.pos = new Vector2(i, j);
                } else if (j == 0) {
                    node.nodeType = NodeType.boundary;
//                    node.pos = new Vector2(i, j);
                } else if (i == mapHeight - 1) {
                    node.nodeType = NodeType.boundary;
//                    node.pos = new Vector2(i, j);
                } else if (j == mapLength - 1) {
                    node.nodeType = NodeType.boundary;
//                    node.pos = new Vector2(i, j);
                } else {
                    node.nodeType = NodeType.movable;
                }
                node.pos = new Vector2(i, j);
                node.aStarState = AStarState.free;
                posXYNodeDict.put(i, j, node);
            }
        }

        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapLength; j++) {
                Node node = (Node)nodes.get(i, j);
                node.neighbourNodes = new Node[4];
                //上
                if (posXYNodeDict.contains(i, j + 1)) {
                    node.neighbourNodes[0] = posXYNodeDict.get(i, j + 1);
                }
                //下
                if (posXYNodeDict.contains(i, j - 1)) {
                    node.neighbourNodes[1] = posXYNodeDict.get(i, j - 1);
                }
                //左
                if (posXYNodeDict.contains(i - 1, j)) {
                    node.neighbourNodes[2] = posXYNodeDict.get(i - 1, j);
                }
                //右
                if (posXYNodeDict.contains(i + 1, j)) {
                    node.neighbourNodes[3] = posXYNodeDict.get(i + 1, j);
                }
            }
        }

        return nodes;
    }

    public void setBlock(Integer r, Integer c) {
        if (posXYNodeDict.contains(r, c)) {
            posXYNodeDict.get(r, c).nodeType = NodeType.block;
        }
    }

    public void setBlock(Integer r, Integer c, NodeType type) {
        if (posXYNodeDict.contains(r, c)) {
            posXYNodeDict.get(r, c).nodeType = type;
        }
    }

    public NodeType getBlock(Integer r, Integer c) {
        return posXYNodeDict.get(r, c).nodeType;
    }
    /// <summary>
    /// 初始化路径节点
    /// </summary>
    public void pathNodeInit(HashBasedTable<Integer, Integer, Node> nodes) {
        for (Integer i : nodes.rowKeySet()) {
            for (Integer j : nodes.columnKeySet() ) {
                Node node = nodes.get(i, j);
                node.init();
            }
        }
    }

    //寻路入口
    public List<Vector2> pathSearch(HashBasedTable<Integer, Integer, Node> nodes, Vector2 startPos, Vector2 endPos) {
        pathNodeInit(nodes);
        return pathSearch(startPos, endPos);
    }

    public List<Vector2> pathSearch(Vector2 startPos, Vector2 endPos) {
        List<Vector2> vec2List = GameUtil.createList();
        pathNodeInit(posXYNodeDict);
        //校验参数
        openList.clear();
        closeList.clear();

        globalEndPos = endPos;
        //算法开始
        //起点为A
        Node A = posXYNodeDict.get((int)startPos.x, (int)startPos.y);
        A.G = 0;
        A.H = Math.abs(globalEndPos.x - A.pos.x) + Math.abs(globalEndPos.y - A.pos.y); //Vector2.distance(A.pos, globalEndPos);
        A.F = A.G + A.H;
        A.parentNode = null;
        closeList.add(A);
        A.aStarState = AStarState.isInCloseList;
        do {
            //遍历OpenList，寻找F值最小的节点，设为A
            if (openList.size() > 0) {
                A = openList.get(0);
            }
            for (int i = 0; i < openList.size(); i++) {
                if (openList.get(i).F < A.F) {
                    A = openList.get(i);
                }
            }

            Node path = aStarSearch(A);

            if (path != null) {
                //print("path found");
                do {
                    path.nodeType = NodeType.aStarPath;
                    if (path.parentNode == null) {
                        path = null;
                    } else {
                        path = path.parentNode;
                        vec2List.add(new Vector2(path.pos.x + 0.5f, path.pos.y));
                    }
                } while (path != null);
                return vec2List;
            }
            openList.remove(A);
            closeList.add(A);
            A.aStarState = AStarState.isInCloseList;
            //OpenList是否还有节点
        } while (openList.size() > 0);
        //无到达目的地的路径

        log.info("path not found");
        return null;
    }

    private Node aStarSearch(Node A) {
        //遍历A的周边节点，当前处理节点为B
        Node B;
        for (int i = 0; i < A.neighbourNodes.length; i++) {
            if (A.neighbourNodes[i] == null) {
                continue;
            }

            B = A.neighbourNodes[i];
            //是否是可移动节点
            if (B.nodeType != NodeType.movable) {
                continue;
            }
            //是否在开放列表中
            if (B.aStarState == AStarState.isInOpenList) {
                //A到B的G值+A.G>B.G
                float curG = (float)Vector2.distance(A.pos, B.pos);
                if (B.G > curG + A.G) {
                //更新B的父节点为A，并相应更新B.G,B.H
                B.parentNode = A;
                B.G = curG + A.G;
                B.F = B.G + B.H;
                }
                continue;
            } else if (B.aStarState == AStarState.free) {
                //更新B的父节点为A，并相应更新B.G; 计算B.F,B.H; B加入OpenList
                B.parentNode = A;
                B.G = (float)Vector2.distance(A.pos, B.pos) + A.G;
                B.H = Math.abs(globalEndPos.x - B.pos.x) + Math.abs(globalEndPos.y - B.pos.y); //Vector2.Distance(B.pos, globalEndPos);
                B.F = B.G + B.H;
                openList.add(B);
                B.aStarState = AStarState.isInOpenList;
                //B.F==0
                if (B.H < Float.MIN_VALUE) {
                    //B的所有父节点既是路径
                    return B;
                } else {
                    //继续遍历
                    continue;
                }
            } else {
                continue;
            }
        }
        return null;
    }

    public boolean checkPath(Integer startX, Integer startY, Integer endX, Integer endY) {
        return checkPath(new Vector2(startX, startY), new Vector2(endX, endY));
    }

    public boolean checkPath(Vector2 startPos, Vector2 endPos) {
        openList.clear();
        closeList.clear();
        globalEndPos = endPos;
        //算法开始
        pathNodeInit(posXYNodeDict);
        //起点为A
        Node A = posXYNodeDict.get((int)startPos.x, (int)startPos.y);
        A.G = 0;
        A.H = Math.abs(globalEndPos.x - A.pos.x) + Math.abs(globalEndPos.y - A.pos.y); //Vector2.distance(A.pos, globalEndPos);
        A.F = A.G + A.H;
        A.parentNode = null;
        closeList.add(A);
        A.aStarState = AStarState.isInCloseList;
        do {
            //遍历OpenList，寻找F值最小的节点，设为A
            if (openList.size() > 0) {
                A = openList.get(0);
            }
            for (int i = 0; i < openList.size(); i++) {
                if (openList.get(i).F < A.F) {
                    A = openList.get(i);
                }
            }

            Node path = aStarSearch(A);

            if (path != null) {
                //print("path found");
                return true;
            }
            openList.remove(A);
            closeList.add(A);
            A.aStarState = AStarState.isInCloseList;
            //OpenList是否还有节点
        } while (openList.size() > 0);
        //无到达目的地的路径

        log.info("path not found");
        return false;
    }

    public void setNodes(HashBasedTable<Integer, Integer, Node> nodes) {
        pathNodeInit(nodes);
    }

    public static void main(String[] args) {
        AStar astar = new AStar();
        HashBasedTable nodes = astar.initMap(8,   6);

        astar.setBlock(4, 0, NodeType.block);
        astar.setBlock(4, 1, NodeType.block);
        astar.setBlock(4, 2, NodeType.block);
        astar.setBlock(4, 3, NodeType.block);
        astar.setBlock(4, 4, NodeType.block);

        {
            List<Vector2> path = astar.pathSearch(nodes, new Vector2(7,2), new Vector2(3,3));
            if (null == path) {
                System.out.println("没有找到路径");
            } else {
                for (Vector2 v3 : path) {
                    System.out.println("    x:" + v3.x + ", y:" + v3.y);
                }
            }
            System.out.println("------------------------------");
        }

        {
            List<Vector2> path = astar.pathSearch(new Vector2(7,2), new Vector2(3,3));
            if (null == path) {
                System.out.println("没有找到路径");
            } else {
                for (Vector2 v3 : path) {
                    System.out.println("    x:" + v3.x + ", y:"+v3.y);
                }
            }
            System.out.println("------------------------------");
        }

        {
            List<Vector2> path = astar.pathSearch(new Vector2(7,2), new Vector2(3,3));
            if (null == path) {
                System.out.println("没有找到路径");
            } else {
                for (Vector2 v3 : path) {
                    System.out.println("    x:" + v3.x + ", y:" + v3.y);
                }
            }
            System.out.println("------------------------------");
        }

        if (astar.checkPath(new Vector2(7,2), new Vector2(3,3))) {
            System.out.println("找到一个路径");
        } else {
            System.out.println("没有找到路径");
        }

        if (astar.checkPath(new Vector2(7,2), new Vector2(3,3))) {
            System.out.println("找到一个路径");
        } else {
            System.out.println("没有找到路径");
        }
    }
}