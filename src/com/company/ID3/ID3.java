package com.company.ID3;


import java.util.ArrayList;
import java.util.HashMap;

public class ID3 {

    /**
     * 属性的名称
     */
    private ArrayList<String> attributes;
    /**
     *  存储的每个属性的值
     */
    private ArrayList<ArrayList<String>> attributeValues;

    /**
     * 子节点对应的数据映射
     */
    private HashMap<String, ArrayList<ArrayList<String>>> subDatasMap;

    /**
     * 决策变量的index
     */
    private int decisionAttrbuteIndex;

    private ID3() {
    }

    // 初始化
    ID3(ArrayList<ArrayList<String>> attributeValues,
        ArrayList<String> attributes) {
        super();
        this.attributeValues = attributeValues;
        this.attributes = attributes;
    }

    // 设置决策属性
    private void setDec(int n) {
        if (n < 0 || n >= attributes.size()) {
            System.err.println("决策变量指定错误。");
            System.exit(2);
        }
        decisionAttrbuteIndex = n;
    }

    public void setDec(String name) {
        int n = attributes.indexOf(name);
        setDec(n);
    }

    public HashMap<String, ArrayList<ArrayList<String>>> getSubDatasMap(int index) {

        ArrayList<String> subSets = DecisionTree.getSubSet(attributeValues, index);
        subDatasMap = new HashMap<>();

        for (String value : subSets) {
            ArrayList<ArrayList<String>> subDatas = new ArrayList<>();
            for (int i=0; i < attributeValues.size(); i++) {
                if (attributeValues.get(i).get(index) == value) {
                    subDatas.add(attributeValues.get(i));
                }
            }

            for (int j=0; j<subDatas.size(); j++) {
                subDatas.get(j).remove(index);
            }
            subDatasMap.put(value, subDatas);
        }

        return subDatasMap;
    }



    // 给一个样本 (各种情况的计数)， 计算它的信息熵
    private double getEntropy(int[] samples) {
        double entropy = 0.0;
        int sum = 0;

        for (int sample : samples) {
            entropy -= sample * Math.log(sample + Double.MIN_VALUE) / Math.log(2);
            sum += sample;
        }

        entropy += sum * Math.log(sum + Double.MIN_VALUE) / Math.log(2);
        entropy /= sum;

        return entropy;
    }

    public double getEntropy(int[] samples, int sum) {
        double entropy = 0.0;

        for (int sample : samples) {
            entropy -= sample * Math.log(sample + Double.MIN_VALUE) / Math.log(2);
        }
        entropy += sum * Math.log(sum+Double.MIN_VALUE) / Math.log(2);
        entropy /= sum;

        return entropy;
    }

    // 获得属性a对样本集D进行划分所获得的  信息增益
    public double getGain(int index) {
        double res;

        int lastIndex = attributeValues.get(0).size()-1;
        // 得到对应index下的属性的 所有值域
        ArrayList<String> subSet = DecisionTree.getSubSet(this.attributeValues, lastIndex);

        int[] samples = new int[subSet.size()];
        for (int i=0; i < subSet.size(); i++) {
            int count = 0;
            for (ArrayList<String> attributeValue : attributeValues) {
                if (attributeValue.get(lastIndex).equals(subSet.get(i))) {
                    count++;
                }
            }
            samples[i] = count;
        }
        res = getEntropy(samples);

        // 计算Ent(D_a)
        ArrayList<String> subSet_a = DecisionTree.getSubSet(this.attributeValues, index);

        samples = new int[subSet_a.size()];

        for (String s : subSet_a) {

            ArrayList<ArrayList<String>> subDatas = new ArrayList<>();
            for (ArrayList<String> attributeValue : attributeValues) {
                if (attributeValue.get(index).equals(s))
                    subDatas.add(attributeValue);
            }

            ArrayList<String> subSetIndex = DecisionTree.getSubSet(subDatas, lastIndex);

            for (int m = 0; m < subSetIndex.size(); m++) {
                int count = 0;
                for (ArrayList<String> subData : subDatas) {
                    if (subData.get(lastIndex).equals(subSetIndex.get(m))) {
                        count++;
                    }
                }
                samples[m] = count;
            }
            double Ent_a = getEntropy(samples);

            res += -1.0 * subSetIndex.size() / attributeValues.size() * Ent_a;
        }

        return res;
    }


    // 计算所有的信息增益，获取最大的一项作为分裂属性
    public int attrSelect() {
        double gain = -1.0;
        int index = 0;
        // 计算信息增益
        System.out.println("attributes : " + this.attributes);
        for (int i=0; i< this.attributes.size()-1; i++) {
            double tempGain = getGain(i);
            if (tempGain > gain) {
                gain = tempGain;
                index = i;
            }

            System.out.println("index : " + index + " i : " + i  + " gain : " + gain + " tempGain : " + tempGain);
        }

        return index;
    }

    public static void main(String[] args) {
        ID3 id3 = new ID3();


        int[] samples = new int[]{
                1,3
        };


        double res = id3.getEntropy(samples);
        System.out.println(res);

    }

}
