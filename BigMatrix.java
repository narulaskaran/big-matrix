/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author karan
 */
public class BigMatrix {

    private HashMap rows;

    public BigMatrix() {
        rows = new HashMap();
    }

    //Set value at a given row and column
    public void setValue(int row, int col, int val) {
        HashMap x = (HashMap) rows.get(row);
        if (x == null) {
            x = new HashMap();
        }

        x.put(col, val);
        rows.put(row, x);
        
    }

    //Returns value at given row and column
    //Return 0 if there is no set value
    public int getValue(int row, int col) {
        HashMap x = (HashMap) rows.get(row);
        if (x == null) {
            return 0;
        }

        if (x.get(col) == null) {
            return 0;
        }

        return (int) x.get(col);
    }

    //Return list of row indices with at least one non-zero value
    public List<Integer> getNonEmptyRows() {
        Iterator it = rows.entrySet().iterator();
        List<Integer> list = new ArrayList<>();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            HashMap x = (HashMap) pair.getValue();
            if (x != null) {
                if (x.size() > 0) {
                    list.add((int) pair.getKey());
                }
            }
        }

        return list;
    }

    //Return list of column indices with at least one non-zero value
    public List<Integer> getNonEmptyCols() {
        Iterator it = rows.entrySet().iterator();
        List<Integer> list = new ArrayList<>();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Iterator iter = ((HashMap) pair.getValue()).entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry p = (Map.Entry) iter.next();
                if ((int)p.getValue() > 0) {
                    int key = (int) p.getKey();
                    if (!list.contains(key)) {
                        list.add(key);
                    }
                }
            }
        }

        return list;
    }

    //Return list of row indices with non-zero value at this column
    public List<Integer> getNonEmptyRowsInColumn(int col) {
        Iterator it = rows.entrySet().iterator();
        List<Integer> list = new ArrayList<>();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            HashMap x = (HashMap) pair.getValue();
            if (x.get(col) != null) {
                list.add((int) pair.getKey());
            }
        }

        return list;
    }

    //Return list of column indices with non-zero value at this column
    public List<Integer> getNonEmptyColsInRow(int row) {
        List<Integer> list = new ArrayList<>();
        HashMap x = (HashMap) rows.get(row);
        if(x == null){
            return list;
        }
        Iterator it = x.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (pair.getValue() != null) {
                list.add((int) pair.getKey());
            }
        }

        return list;
    }

    //Return sum of all values in this row
    public int getRowSum(int row) {
        HashMap x = (HashMap) rows.get(row);
        if(x == null){
            return 0;
        }
        Iterator it = x.entrySet().iterator();
        int sum = 0;

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            sum += (int) pair.getValue();
        }

        return sum;
    }

    //Return sum of all values in this column
    public int getColSum(int col) {
        Iterator it = rows.entrySet().iterator();
        int sum = 0;

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            HashMap row = (HashMap) pair.getValue();
            if (row.get(col) != null) {
                sum += (int) row.get(col);
            }
        }

        return sum;
    }

    //Return sum of all values in the matrix
    public int getTotalSum() {
        Iterator it = rows.entrySet().iterator();
        int sum = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            sum += getRowSum((int) pair.getKey());
        }

        return sum;
    }

    //Return new matrix with each value multiplied by a constant
    public BigMatrix multiplyByConstant(int constant) {
        if (constant == 1) {
            return this;
        } else if (constant == 0) {
            return new BigMatrix();
        }
        
//        BigMatrix newMatrix = new BigMatrix();
//        List<Integer> r = getNonEmptyRows();
//        for(int a : r){
//            List<Integer> c = getNonEmptyColsInRow(a);
//            for(int b : c){
//                newMatrix.setValue(a, b, getValue(a,b)*constant);
//            }
//        }

        BigMatrix newMatrix = new BigMatrix();
        Iterator outer = rows.entrySet().iterator();
        Iterator inner;

        while (outer.hasNext()) {
            Map.Entry outerPair = (Map.Entry) outer.next();
            HashMap row = (HashMap) outerPair.getValue();
            inner = row.entrySet().iterator();

            while (inner.hasNext()) {
                Map.Entry pair = (Map.Entry) inner.next();
                newMatrix.setValue((int) outerPair.getKey(), (int) pair.getKey(), constant * (int) pair.getValue());
            }
        }

        newMatrix.prune();
        return newMatrix;

    }

    //Return new matix with values the sums of original plus "other"
    public BigMatrix addMatrix(BigMatrix other) {
        BigMatrix toReturn = this.copy();

        List<Integer> r = other.getNonEmptyRows();
        List<Integer> c = other.getNonEmptyCols();

        for (int a : r) {
            for (int b : c) {
                int original = toReturn.getValue(a, b);
                int passed = other.getValue(a, b);
                toReturn.setValue(a, b, original + passed);
            }
        }

        toReturn.prune();
        return toReturn;
    }

    //Return a new copy of the matrix
    private BigMatrix copy() {
        BigMatrix toReturn = new BigMatrix();
        Iterator outer = rows.entrySet().iterator();
        Iterator inner;
        while (outer.hasNext()) {
            Map.Entry outerPair = (Map.Entry) outer.next();
            HashMap row = (HashMap) outerPair.getValue();
            inner = row.entrySet().iterator();
            while (inner.hasNext()) {
                Map.Entry pair = (Map.Entry) inner.next();
                toReturn.setValue((int) outerPair.getKey(), (int) pair.getKey(), (int) pair.getValue());
            }
        }
        
        toReturn.prune();
        return toReturn;
    }

    //Remove all 0 values 
    private void prune() {
        List<Integer> r = this.getNonEmptyRows();
        List<Integer> c = this.getNonEmptyCols();
        for (int a : r) {
            for (int b : c) {
                if (this.getValue(a, b) == 0) {
                    ((HashMap) rows.get(a)).remove(b);
                }
            }
        }
        
        List<Integer> emptyRows = new ArrayList<>();
        Iterator out = rows.entrySet().iterator();
        while(out.hasNext()){
            Map.Entry pair = (Map.Entry)out.next();
            HashMap row = (HashMap) pair.getValue();
            
            if(row.size() == 0){
                emptyRows.add((int)pair.getKey());
            }
        }
        
        for(int row : emptyRows){
            rows.remove(row);
        }
    }

    public static void main(String[] args) {

        BigMatrix b = new BigMatrix();
        b.setValue(100, 50, 3);
        b.setValue(50, 100, 5);
        b.setValue(5, 100, 4);
        b.setValue(100, 75, 2);

        BigMatrix a = b.multiplyByConstant(2);

        BigMatrix c = b.addMatrix(a);

    }

}
