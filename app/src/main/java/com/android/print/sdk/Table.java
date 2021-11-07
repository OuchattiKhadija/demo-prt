package com.android.print.sdk;

import android.annotation.SuppressLint;
import com.android.print.sdk.util.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint({"UseSparseArrays"})
public class Table {
    private boolean alignRight;
    private int[] tableColWidth;
    private String tableReg;
    private List<String> tableRows = new ArrayList();
    private HashMap<Integer, String> unPrintColumnMap = new HashMap<>();

    public Table(String column, String regularExpression, int[] columnWidth) {
        this.tableRows.add(column);
        this.tableReg = regularExpression;
        if (columnWidth != null) {
            this.tableColWidth = columnWidth;
            return;
        }
        this.tableColWidth = new int[column.split(regularExpression).length];
        for (int i = 0; i < this.tableColWidth.length; i++) {
            this.tableColWidth[i] = 8;
        }
    }

    public void setColumnAlignRight(boolean right) {
        this.alignRight = right;
    }

    public void addRow(String row) {
        if (this.tableRows != null) {
            this.tableRows.add(row);
        }
    }

    public String getTableText() {
        StringBuffer sb = new StringBuffer();
        for (int m = 0; m < this.tableRows.size(); m++) {
            sb.append(printTableLine(this.tableRows.get(m).split(this.tableReg)));
            sb.append("\n");
        }
        return sb.toString();
    }

    private String printTableLine(String[] tableLine) {
        StringBuffer sb = new StringBuffer();
        String[] line = tableLine;
        int i = 0;
        while (i < line.length) {
            line[i] = line[i].trim();
            int index = line[i].indexOf("\n");
            if (index != -1) {
                this.unPrintColumnMap.put(Integer.valueOf(i), line[i].substring(index + 1));
                line[i] = line[i].substring(0, index);
                sb.append(printTableLine(line));
                sb.append(printNewLine(line));
                return sb.toString();
            }
            int length = Utils.getStringCharacterLength(line[i]);
            int col_width = 8;
            if (i < this.tableColWidth.length) {
                col_width = this.tableColWidth[i];
            }
            if (length <= col_width || i == line.length - 1) {
                if (i == 0) {
                    sb.append(line[i]);
                    for (int j = 0; j < col_width - length; j++) {
                        sb.append(" ");
                    }
                } else if (this.alignRight) {
                    for (int j2 = 0; j2 < col_width - length; j2++) {
                        sb.append(" ");
                    }
                    sb.append(line[i]);
                } else {
                    sb.append(line[i]);
                    for (int j3 = 0; j3 < col_width - length; j3++) {
                        if (i != line.length - 1) {
                            sb.append(" ");
                        }
                    }
                }
                i++;
            } else {
                int sub_length = Utils.getSubLength(line[i], col_width);
                this.unPrintColumnMap.put(Integer.valueOf(i), line[i].substring(sub_length, line[i].length()));
                line[i] = line[i].substring(0, sub_length);
                StringBuffer sb2 = new StringBuffer();
                sb2.append(printTableLine(line));
                sb2.append(printNewLine(line));
                return sb2.toString();
            }
        }
        return sb.toString();
    }

    private String printNewLine(String[] oldLine) {
        if (this.unPrintColumnMap.isEmpty()) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        String[] newLine = new String[oldLine.length];
        for (Map.Entry entry : this.unPrintColumnMap.entrySet()) {
            Integer key = (Integer) entry.getKey();
            String value = (String) entry.getValue();
            if (key.intValue() < oldLine.length) {
                newLine[key.intValue()] = value;
            }
        }
        this.unPrintColumnMap.clear();
        for (int i = 0; i < newLine.length; i++) {
            if (newLine[i] == null || newLine[i] == "") {
                newLine[i] = " ";
            }
        }
        sb.append(printTableLine(newLine));
        return "\n" + sb.toString();
    }
}
