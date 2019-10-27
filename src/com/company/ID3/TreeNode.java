package com.company.ID3;

import java.util.ArrayList;

public class TreeNode {

    /**
     * 该节点的名称
     *  分裂的属性
     */
    private String name;

    /**
     * 分裂的属性的值域
     */
    private ArrayList<String> splitAttr;

    /**
     * 子节点
     */
    private ArrayList<TreeNode> children;

    public TreeNode()
    {
        this.name = "";
        this.splitAttr = new ArrayList<String>();
        this.children = new ArrayList<TreeNode>();
    }


    public TreeNode(String name, ArrayList<String> splitAttr, ArrayList<TreeNode> children) {
        this.name = name;
        this.splitAttr = splitAttr;
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getSplitAttr() {
        return splitAttr;
    }

    public void setSplitAttr(ArrayList<String> splitAttr) {
        this.splitAttr = splitAttr;
    }

    public ArrayList<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<TreeNode> children) {
        this.children = children;
    }


}
