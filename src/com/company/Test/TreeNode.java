package com.company.Test;


import java.util.ArrayList;

public class TreeNode
{
    private String name; 								// 该结点的名称（分裂属性）
    private ArrayList<String> rules; 				// 结点的分裂规则(假设均为离散值)
    //	private ArrayList<ArrayList<String>> datas; 	// 划分到该结点的训练元组（datas.get(i)表示一个训练元组）
//	private ArrayList<String> candidateAttributes; // 划分到该结点的候选属性（与训练元组的个数一致）
    private ArrayList<TreeNode> children; 			// 子结点

    public TreeNode()
    {
        this.name = "";
        this.rules = new ArrayList<String>();
        this.children = new ArrayList<TreeNode>();
//		this.datas = null;
//		this.candidateAttributes = null;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public ArrayList<String> getRules()
    {
        return rules;
    }

    public void setRules(ArrayList<String> rules)
    {
        this.rules = rules;
    }

    public ArrayList<TreeNode> getChildren()
    {
        return children;
    }

    public void setChildren(ArrayList<TreeNode> children)
    {
        this.children = children;
    }

//	public ArrayList<ArrayList<String>> getDatas()
//	{
//		return datas;
//	}
//
//	public void setDatas(ArrayList<ArrayList<String>> datas)
//	{
//		this.datas = datas;
//	}
//
//	public ArrayList<String> getCandidateAttributes()
//	{
//		return candidateAttributes;
//	}
//
//	public void setCandidateAttributes(ArrayList<String> candidateAttributes)
//	{
//		this.candidateAttributes = candidateAttributes;
//	}


}