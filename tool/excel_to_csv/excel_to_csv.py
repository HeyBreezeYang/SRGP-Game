#coding=utf-8
import os
import time
import logging
import xlrd
import csv
import sys

INPUTPATH= u"D:\\lsssl\\桌面\\xls文件"
 
OUTPATH = u"D:\\lsssl\桌面\\csv"
 
 
class changeCenter:
    def __init__(self):
        pass
    def getvalue(self,filename):
        print "filename:" + filename
        self.mData = []
        xlsfile=xlrd.open_workbook(filename)
        table = xlsfile.sheet_by_index(0)#sheet1
        rownum = table.nrows #行
        colsnum = table.ncols #列
        for i in range(0,rownum):
            row = []
            for j in range(0,colsnum):
                value = table.cell_value(i,j)
                if not isinstance(value,float):
                    value = value.encode('gbk')#非数字转一下码
                else:
                    value = int(value)
                row.append(value)
            self.mData.append(tuple(row))
    def write(self, path, filename):
        if not os.path.exists(path):
            os.makedirs(path)
        csvfile = file("tmp","wb")
        writer = csv.writer(csvfile)
        writer.writerows(self.mData)
        csvfile.close()
        
        if os.path.exists(os.path.join(path,filename+".old")):
            os.remove(os.path.join(path,filename+".old"))
        if os.path.exists(os.path.join(path,filename)):
            os.remove(os.path.join(path,filename))
        os.rename('tmp', os.path.join(path,filename))
        logging.info("write file finish")
        print "write",filename," finish"
 
 
def handleExcel():
    files,dirs,root = readFilename(INPUTPATH)
    for fi in files:
        strstock = os.path.join(INPUTPATH,fi)
        ext = os.path.splitext(strstock)[-1]
        if os.path.exists(strstock) and ( ext == ".xlsx" or ext == ".xls"):
            st = changeCenter()
            st.getvalue(strstock)
            name = fi.replace(ext,"")
            st.write(OUTPATH, name+".csv")
        else:
            print strstock+" don't exist"
 
#获取某个路径下的所有文件   
def readFilename(file_dir):
    for root, dirs, files in os.walk(file_dir): 
        return files,dirs,root
    
if __name__ == '__main__':
    if len(sys.argv) < 3:
        sys.exit(2) 
    INPUTPATH = sys.argv[1]
    OUTPATH = sys.argv[2]
    handleExcel()