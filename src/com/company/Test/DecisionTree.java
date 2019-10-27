package com.company.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

public class DecisionTree {

    private ArrayList<ArrayList<String>> allDatas;
    private ArrayList<String> allAttributes;

    /**
     * 从文件中读取所有相关数据
     * @param dataFilePath
     * @param attrFilePath
     */
    public DecisionTree(String dataFilePath,String attrFilePath)
    {
        super();

        try
        {
            this.allDatas = new ArrayList<>();
            this.allAttributes = new ArrayList<>();

            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(new File(dataFilePath)));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = null;
            while((line = bufferedReader.readLine())!=null)
            {
                String[] strings = line.split(",");
                ArrayList<String> data = new ArrayList<>();
                for(int i=0;i<strings.length;i++)
                    data.add(strings[i]);
                this.allDatas.add(data);
            }


            inputStreamReader = new InputStreamReader(new FileInputStream(new File(attrFilePath)));
            bufferedReader = new BufferedReader(inputStreamReader);
            while((line=bufferedReader.readLine())!=null)
            {
                String[] strings = line.split(",");
                for(int i=0;i<strings.length;i++)
                    this.allAttributes.add(strings[i]);
            }

            inputStreamReader.close();
            bufferedReader.close();

        } catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


//		for(int i=0;i<this.allAttributes.size();i++)
//		{
//			System.out.print(this.allAttributes.get(i)+" ");
//		}
//		System.out.println();
//
//		for(int i=0;i<this.allDatas.size();i++)
//		{
//			for(int j=0;j<this.allDatas.get(i).size();j++)
//			{
//				System.out.print(this.allDatas.get(i).get(j)+" ");
//			}
//			System.out.println();
//		}

    }

    /**
     * @param allDatas
     * @param allAttributes
     */
    public DecisionTree(ArrayList<ArrayList<String>> allDatas,
                        ArrayList<String> allAttributes)
    {
        super();
        this.allDatas = allDatas;
        this.allAttributes = allAttributes;
    }

    public ArrayList<ArrayList<String>> getAllDatas()
    {
        return allDatas;
    }

    public void setAllDatas(ArrayList<ArrayList<String>> allDatas)
    {
        this.allDatas = allDatas;
    }

    public ArrayList<String> getAllAttributes()
    {
        return allAttributes;
    }

    public void setAllAttributes(ArrayList<String> allAttributes)
    {
        this.allAttributes = allAttributes;
    }



    /**
     * 递归生成决策数
     * @return
     */
    public static TreeNode generateDecisionTree(ArrayList<ArrayList<String>> datas, ArrayList<String> attrs)
    {
        TreeNode treeNode = new TreeNode();

        //如果D中的元素都在同一类C中，then
        if(isInTheSameClass(datas))
        {
            treeNode.setName(datas.get(0).get(datas.get(0).size()-1));
//			rootNode.setName();
            return treeNode;
        }
        //如果attrs为空，then(这种情况一般不会出现，我们应该是要对所有的候选属性集合构建决策树)
        if(attrs.size() == 0)
            return treeNode;

        CriterionID3 criterionID3 = new CriterionID3(datas, attrs);
        int splitingCriterionIndex = criterionID3.attributeSelectionMethod();

        treeNode.setName(attrs.get(splitingCriterionIndex));
        treeNode.setRules(getValueSet(datas, splitingCriterionIndex));

        attrs.remove(splitingCriterionIndex);

        Map<String, ArrayList<ArrayList<String>>> subDatasMap = criterionID3.getSubDatasMap(splitingCriterionIndex);
//		for(String key:subDatasMap.keySet())
//		{
//			System.out.println("===========");
//			System.out.println(key);
//			for(int i=0;i<subDatasMap.get(key).size();i++)
//			{
//				for(int j=0;j<subDatasMap.get(key).get(i).size();j++)
//				{
//					System.out.print(subDatasMap.get(key).get(i).get(j)+" ");
//				}
//				System.out.println();
//			}
//		}

        for(String key:subDatasMap.keySet())
        {
            ArrayList<TreeNode> treeNodes = treeNode.getChildren();
            treeNodes.add(generateDecisionTree(subDatasMap.get(key), attrs));
            treeNode.setChildren(treeNodes);
        }

        return treeNode;
    }




    /**
     * 获取datas中index列的值域
     * @param
     * @param index
     * @return
     */
    public static ArrayList<String> getValueSet(ArrayList<ArrayList<String>> datas,int index)
    {
        ArrayList<String> values = new ArrayList<String>();
        String r = "";
        for (int i = 0; i < datas.size(); i++) {
            r = datas.get(i).get(index);
            if (!values.contains(r)) {
                values.add(r);
            }
        }
        return values;
    }

    /**
     * 最后一列是类标号，判断最后一列是否相同
     * @param datas
     * @return
     */
    private static boolean isInTheSameClass(ArrayList<ArrayList<String>> datas)
    {
        String flag = datas.get(0).get(datas.get(0).size()-1);//第0行，最后一列赋初值
        for(int i=0; i<datas.size(); i++)
        {
            if(!datas.get(i).get(datas.get(i).size()-1).equals(flag))
                return false;
        }
        return true;
    }


    public static void main(String[] args)
    {
        String dataPath = "files/data.txt";
        String attrPath = "files/attr.txt";

        //初始化原始数据
        DecisionTree decisionTree = new DecisionTree(dataPath,attrPath);

        //生成决策树
        TreeNode treeNode = generateDecisionTree(decisionTree.getAllDatas(), decisionTree.getAllAttributes());

        print(treeNode,0);
    }

    private static void print(TreeNode treeNode,int level)
    {
        for(int i=0;i<level;i++)
            System.out.print("\t");
        System.out.print(treeNode.getName());
        System.out.print("(");
        for(int i=0;i<treeNode.getRules().size();i++)
            System.out.print((i+1)+":"+treeNode.getRules().get(i)+"; ");
        System.out.println(")");

        ArrayList<TreeNode> treeNodes = treeNode.getChildren();
        for(int i=0;i<treeNodes.size();i++)
        {
            print(treeNodes.get(i),level+1);
        }
    }


}

