package com.company.ID3;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DecisionTree {
    /**
     * 所有的属性值
     * 例：青绿，蜷缩，浊响，清晰，凹陷，硬划，是
     *  ...
     */
    private ArrayList<ArrayList<String>> attributeValues;

    /**
     * 属性名称
     * 例：色泽、根蒂、敲声、纹理、脐部、触感、好瓜
     */
    private ArrayList<String> attributes;

    private static final String prefix = "D:\\git_workspace\\";
    // 用于找到属性 进行匹配
    public static final String patternStr = "@attribute(.*)[{](.*?)[}]";


    public DecisionTree(String filePath) {
        // 从文件中 读取数据，并初始化基本属性
        attributes = new ArrayList<>();
        attributeValues = new ArrayList<>();
        try(FileReader fr = new FileReader(new File(filePath));
            BufferedReader br = new BufferedReader(fr);
            ) {
            String line;
            Pattern pattern = Pattern.compile(patternStr);

            while ((line = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                // 如果匹配的上
                if (matcher.find()) {
                    System.out.println(matcher.group(1).trim());
                    attributes.add(matcher.group(1).trim());
                } else if (line.startsWith("@data")) {

                    while ((line = br.readLine()) != null) {
                        if (line == null || line == "") {
                            continue;
                        }

                        String[] row = line.split(",");
                        ArrayList<String> temp = new ArrayList<String>(Arrays.asList(row));
                        attributeValues.add(temp);
                    }
                } else {
                    continue;
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public DecisionTree(ArrayList<ArrayList<String>> attributeValues, ArrayList<String> attributes) {
        super();
        this.attributeValues = attributeValues;
        this.attributes = attributes;
    }

    /**
     * 生成决策树
     */
    public static TreeNode createDecisionTree(ArrayList<ArrayList<String>> attributeValues, ArrayList<String> attributes) {

        TreeNode node = new TreeNode();

        if(attributes.size() == 0)
            return node;

        ID3 id3 = new ID3(attributeValues, attributes);

        // 获取熵信息最大的那个属性的下表
        int index = id3.attrSelect();
        node.setName(attributes.get(index));
        node.setSplitAttr(getSubSet(attributeValues, index));
        System.out.println("index : " + index);
        System.out.println("name : " + attributes.get(index));
        System.out.println("attributes : " + attributes);
        attributes.remove(index);

        // 获取有index下表对应属性的所有其他属性值
        HashMap<String, ArrayList<ArrayList<String>>> subDatasMap = id3.getSubDatasMap(index);
        System.out.println("size : " + subDatasMap.size());
        // 递归生成决策树
        for (String key : subDatasMap.keySet()) {
            ArrayList<TreeNode> nodes = node.getChildren();
            // 将剩余的属性生成决策树
            System.out.println(" subDatasMap : " + subDatasMap.get(key));
            nodes.add(createDecisionTree(subDatasMap.get(key),attributes));
            node.setChildren(nodes);
        }

        return node;
    }

    /**
     * 打印决策树
     */
    private static void printDecisionTree(TreeNode node, int level) {
        for(int i=0;i<level;i++)
            System.out.print("\t");
        System.out.print(node.getName());
        System.out.print("(");
        for(int i=0;i<node.getSplitAttr().size();i++)
            System.out.print((i+1) + ":" + node.getSplitAttr().get(i)+"; ");
        System.out.println(")");

        ArrayList<TreeNode> treeNodes = node.getChildren();
        for(int i=0;i<treeNodes.size();i++)
        {
            printDecisionTree(treeNodes.get(i),level+1);
        }
    }

    /**
     *  得到对应index下属性的 所有值域
     *  例如："youth", "high", "no", "fair", "no"
     *       "youth", "medium","no","fair","yes"   ---->>>
     *      index = 1
     *
     *      res --- >>> { high, mediun }
     *
     */
    public static ArrayList<String> getSubSet(ArrayList<ArrayList<String>> datas, int index) {

        ArrayList<String> res = new ArrayList<>();
        for (int i=0; i<datas.size(); i++) {
            String s = datas.get(i).get(index);
            if (!res.contains(s)) {
                res.add(s);
            }
        }
        return res;
    }


    public ArrayList<ArrayList<String>> getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(ArrayList<ArrayList<String>> attributeValues) {
        this.attributeValues = attributeValues;
    }

    public ArrayList<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(ArrayList<String> attributes) {
        this.attributes = attributes;
    }


    public static void main(String[] args) {
        /*ArrayList<ArrayList<String>> attributeValues = new ArrayList<>();

        ArrayList<String> s1 = new ArrayList<String>(Arrays.asList(new String[]{"youth", "high", "no", "fair", "no"}));
        ArrayList<String> s2 = new ArrayList<String>(Arrays.asList(new String[]{"middle_aged","high","no","fair","yes"}));
        ArrayList<String> s3 = new ArrayList<String>(Arrays.asList(new String[]{"youth", "medium","no","fair","yes"}));
        ArrayList<String> s4 = new ArrayList<String>(Arrays.asList(new String[]{"senior", "low","yes","fair","yes"}));
        ArrayList<String> s5 = new ArrayList<String>(Arrays.asList(new String[]{"senior", "low","yes","excellent","no"}));
        ArrayList<String> s6 = new ArrayList<String>(Arrays.asList(new String[]{"youth", "high","no","fair","no"}));
        ArrayList<String> s7 = new ArrayList<String>(Arrays.asList(new String[]{"youth","high","no","excellent","no"}));

        attributeValues.add(s1);
        attributeValues.add(s2);
        attributeValues.add(s3);
        attributeValues.add(s4);
        attributeValues.add(s5);
        attributeValues.add(s6);
        attributeValues.add(s7);

        ArrayList<String> attributes = new ArrayList<>();
        attributes.add("age");
        attributes.add("income");
        attributes.add("student");
        attributes.add("credit_rating");
        attributes.add("buys_computer");

        //attributeValues.add((ArrayList<String>)Arrays.asList(str2));
        //attributeValues.add((ArrayList<String>)Arrays.asList(str3));
       // attributeValues.add((ArrayList<String>)Arrays.asList(str4));
       // attributeValues.add((ArrayList<String>)Arrays.asList(str5));
       // attributeValues.add((ArrayList<String>)Arrays.asList(str6));
      // System.out.println(attributeValues);
       DecisionTree decisionTree = new DecisionTree(attributeValues, null);*/
      // ArrayList<String> res = decisionTree.getSubSet(attributeValues, 4);
      // System.out.println(res);

      /* ID3 id3 = new ID3(attributeValues, attributes);
       double result = id3.getGain(1);
       System.out.println(result);

       HashMap<String, ArrayList<ArrayList<String>>> map = id3.getSubDatasMap(1);
       for (String key : map.keySet()) {
           ArrayList<ArrayList<String>> subDatas = map.get(key);
           for (int i=0; i<subDatas.size(); i++) {
               System.out.println(subDatas.get(i));
           }
       }*/

        String filePath = prefix + "data.arff";
        DecisionTree dt = new DecisionTree(filePath);
        /**System.out.println(dt.attributes);
        for (ArrayList<String> values : dt.attributeValues) {
            System.out.println(values);
        }
        System.out.println();*/


        //生成决策树
        TreeNode treeNode = createDecisionTree(dt.getAttributeValues(), dt.getAttributes());

        printDecisionTree(treeNode,0);

    }
}
