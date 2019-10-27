package com.company.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CriterionID3 {
    private ArrayList<ArrayList<String>> datas;
    private ArrayList<String> attributes;

    private Map<String, ArrayList<ArrayList<String>>> subDatasMap;

    /**
     * 计算所有的信息增益，获取最大的一项作为分裂属性
     *
     * @return
     */
    public int attributeSelectionMethod() {
        double gain = -1.0;
        int maxIndex = 0;
        for (int i = 0; i < this.attributes.size() - 1; i++) {
            double tempGain = this.calcGain(i);
            if (tempGain > gain) {
                gain = tempGain;
                maxIndex = i;
            }
        }

        return maxIndex;
    }

    /**
     * 计算 Gain(age)=Info(D)-Info_age(D) 等
     * @param index
     * @return
     */
    /**
     * @param index
     * @param
     * @return
     */
    private double calcGain(int index) {
        double result = 0;

        //计算Info(D)
        int lastIndex = datas.get(0).size() - 1;
        // 得到对应index下的属性的 所有值域
        ArrayList<String> valueSet = DecisionTree.getValueSet(this.datas, lastIndex);
        for (String value : valueSet) {
            int count = 0;
            for (int i = 0; i < datas.size(); i++) {
                if (datas.get(i).get(lastIndex).equals(value))
                    count++;
            }

            result += -(1.0 * count / datas.size()) * Math.log(1.0 * count / datas.size()) / Math.log(2);
//			System.out.println(result);
        }
//		System.out.println("==========");

        //计算Info_a(D)
        valueSet = DecisionTree.getValueSet(this.datas, index);

//		for(String temp:valueSet)
//			System.out.println(temp);
//		System.out.println("==========");

        for (String value : valueSet) {
            ArrayList<ArrayList<String>> subDatas = new ArrayList<>();
            for (int i = 0; i < datas.size(); i++) {
                if (datas.get(i).get(index).equals(value))
                    subDatas.add(datas.get(i));
            }


//			for(ArrayList<String> temp:subDatas)
//			{
//				for(String temp2:temp)
//					System.out.print(temp2+" ");
//				System.out.println();
//			}

            ArrayList<String> subValueSet = DecisionTree.getValueSet(subDatas, lastIndex);


//			System.out.print("subValueSet:");
//			for(String temp:subValueSet)
//				System.out.print(temp+" ");
//			System.out.println();


            for (String subValue : subValueSet) {
//				System.out.println("+++++++++++++++");
//				System.out.println(subValue);
                int count = 0;
                for (int i = 0; i < subDatas.size(); i++) {
                    if (subDatas.get(i).get(lastIndex).equals(subValue))
                        count++;
                }
//				System.out.println(count);
                result += -1.0 * subDatas.size() / datas.size() * (-(1.0 * count / subDatas.size()) * Math.log(1.0 * count / subDatas.size()) / Math.log(2));
//				System.out.println(result);
            }

        }

        return result;

    }


    public CriterionID3(ArrayList<ArrayList<String>> datas,
                        ArrayList<String> attributes) {
        super();
        this.datas = datas;
        this.attributes = attributes;
    }


    public ArrayList<ArrayList<String>> getDatas() {
        return datas;
    }


    public void setDatas(ArrayList<ArrayList<String>> datas) {
        this.datas = datas;
    }


    public ArrayList<String> getAttributes() {
        return attributes;
    }


    public void setAttributes(ArrayList<String> attributes) {
        this.attributes = attributes;
    }


    /**
     * 去掉index对应的属性后  还剩下的属性存在map中 用于下次递归生成决策树
     */
    public Map<String, ArrayList<ArrayList<String>>> getSubDatasMap(int index) {
        ArrayList<String> valueSet = DecisionTree.getValueSet(this.datas, index);
        this.subDatasMap = new HashMap<String, ArrayList<ArrayList<String>>>();

        for (String value : valueSet) {
            ArrayList<ArrayList<String>> subDatas = new ArrayList<>();
            for (int i = 0; i < this.datas.size(); i++) {
                if (this.datas.get(i).get(index).equals(value))
                    subDatas.add(this.datas.get(i));
            }
            for (int i = 0; i < subDatas.size(); i++) {
                subDatas.get(i).remove(index);
            }
            this.subDatasMap.put(value, subDatas);
        }

        return subDatasMap;
    }

    public void setSubDatasMap(Map<String, ArrayList<ArrayList<String>>> subDatasMap) {
        this.subDatasMap = subDatasMap;
    }
}