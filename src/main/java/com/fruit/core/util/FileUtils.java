package com.fruit.core.util;

/**
 * Copyright: Copyright (c) 2002-2003
 * Company: JavaResearch(http://www.javaresearch.org)
 * 最后更新日期:2003年5月18日
 * @author Cherami(弱啊)copy方法有点问题
 * @author Lzj.Liu
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.filechooser.FileFilter;

/**
 * 此类中封装一些常用的文件操作。
 * 所有方法都是静态方法，不需要生成此类的实例，
 * 为避免生成此类的实例，构造方法被申明为private类型的。
 * @since  0.1
 */

public class FileUtils
{
    /**
     * 私有构造方法，防止类的实例化，因为工具类不需要实例化。
     */
    private FileUtils()
    {

    }
    /**
	 * 根据需要创建文件夹
	 * 
	 * @param dirPath
	 *            文件夹路径
	 * @param del
	 *            存在文件夹是否删除
	 */
	public static void mkdir(String dirPath, boolean del) {
		File dir = new File(dirPath);
		if (dir.exists()) {
			if (del)
				dir.delete();
			else
				return;
		}
		if (!dir.getParentFile().exists()) {
			dir.getParentFile().mkdirs();
		}
	}

 
    public static boolean expiredFile(Long modifiedTimeMS,int minutes) {
    	long maxMillis=modifiedTimeMS + minutes*60*1000;
    	long nowMillis=System.currentTimeMillis();
		return maxMillis< nowMillis; 
	}
   
    /**
     * 修改文件的最后访问时间。
     * 如果文件不存在则创建该文件。
     * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b>
     * @param file 需要修改最后访问时间的文件。
     * @since  0.1
     */
    public static void touch(File file)
    {
        long currentTime = System.currentTimeMillis();
        if (!file.exists())
        {
            try
            {
            	if (!file.getParentFile().exists()) {
            		file.getParentFile().mkdirs();
        		}
            }
            catch (Exception e)
            {
                System.err.println("Create file failed!");
                e.printStackTrace();
            }
        }
        boolean result = file.setLastModified(currentTime);
        if (!result)
        {
            System.err.println("touch failed: " + file.getName());
        }
    }

    /**
     * 修改文件的最后访问时间。
     * 如果文件不存在则创建该文件。
     * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b>
     * @param fileName 需要修改最后访问时间的文件的文件名。
     * @since  0.1
     */
    public static void touch(String fileName)
    {
        File file = new File(fileName);
        touch(file);
    }

    /**
     * 修改文件的最后访问时间。
     * 如果文件不存在则创建该文件。
     * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b>
     * @param files 需要修改最后访问时间的文件数组。
     * @since  0.1
     */
    public static void touch(File[] files)
    {
        for (int i = 0; i < files.length; i++)
        {
            touch(files[i]);
        }
    }

    /**
     * 修改文件的最后访问时间。
     * 如果文件不存在则创建该文件。
     * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考虑中。</b>
     * @param fileNames 需要修改最后访问时间的文件名数组。
     * @since  0.1
     */
    public static void touch(String[] fileNames)
    {
        File[] files = new File[fileNames.length];
        for (int i = 0; i < fileNames.length; i++)
        {
            files[i] = new File(fileNames[i]);
        }
        touch(files);
    }

    /**
     * 判断指定的文件是否存在。
     * @param fileName 要判断的文件的文件名
     * @return 存在时返回true，否则返回false。
     * @since  0.1
     */
    public static boolean isFileExist(String fileName)
    {
    	
        return new File(fileName).exists();
    }

    /**
     * 创建指定的目录。
     * 如果指定的目录的父目录不存在则创建其目录书上所有需要的父目录。
     * <b>注意：可能会在返回false的时候创建部分父目录。</b>
     * @param file 要创建的目录
     * @return 完全创建成功时返回true，否则返回false。
     * @since  0.1
     */
    public static boolean makeDirectory(File file)
    {
        //File parent = file.getParentFile();
        //if (parent != null) 
        //{
        return file.mkdirs();
        //}
        //return false;
    }

    /**
     * 创建指定的目录。
     * 如果指定的目录的父目录不存在则创建其目录书上所有需要的父目录。
     * <b>注意：可能会在返回false的时候创建部分父目录。</b>
     * @param fileName 要创建的目录的目录名
     * @return 完全创建成功时返回true，否则返回false。
     * @since  0.1
     */
    public static boolean makeDirectory(String fileName)
    {
        boolean bTmp = false;
        try
        {
            File file = new File(fileName.toString());
            makeDirectory(file);
            bTmp = true;
        }
        catch (Exception ex)
        {
            bTmp = false;
            System.out.println(ex.toString());
        }
        return bTmp;
    }

    /**
     * 清空指定目录中的文件。
     * 这个方法将尽可能删除所有的文件，但是只要有一个文件没有被删除都会返回false。
     * 另外这个方法不会迭代删除，即不会删除子目录及其内容。
     * @param directory 要清空的目录
     * @return 目录下的所有文件都被成功删除时返回true，否则返回false.
     * @since  0.1
     */
    public static boolean emptyDirectory(File directory)
    {
        boolean result = false;
        File[] entries = directory.listFiles();
        for (int i = 0; i < entries.length; i++)
        {
            if (!entries[i].delete())
            {
                result = false;
            }
        }
        return result;
    }

    /**
     * 清空指定目录中的文件。
     * 这个方法将尽可能删除所有的文件，但是只要有一个文件没有被删除都会返回false。
     * 另外这个方法不会迭代删除，即不会删除子目录及其内容。
     * @param directoryName 要清空的目录的目录名
     * @return 目录下的所有文件都被成功删除时返回true，否则返回false。
     * @since  0.1
     */
    public static boolean emptyDirectory(String directoryName)
    {
        File dir = new File(directoryName);
        return emptyDirectory(dir);
    }

    /**
     * 删除指定目录及其中的所有内容。
     * @param dirName 要删除的目录的目录名
     * @return 删除成功时返回true，否则返回false。
     * @since  0.1
     */
    public static boolean deleteDirectory(String dirName)
    {
        return deleteDirectory(new File(dirName));
    }

    /**
     * 删除指定目录及其中的所有内容。
     * @param dir 要删除的目录
     * @return 删除成功时返回true，否则返回false。
     * @since  0.1
     */
    public static boolean deleteDirectory(File dir)
    {
        if ((dir == null) || !dir.isDirectory()) { throw new IllegalArgumentException("Argument "
                + dir + " is not a directory. "); }

        File[] entries = dir.listFiles();
        int sz = entries.length;

        for (int i = 0; i < sz; i++)
        {
            if (entries[i].isDirectory())
            {
                if (!deleteDirectory(entries[i])) { return false; }
            }
            else
            {
                if (!entries[i].delete()) { return false; }
            }
        }

        if (!dir.delete()) { return false; }
        return true;
    }

    /**
     * 列出目录中的所有内容，包括其子目录中的内容。
     * @param fileName 要列出的目录的目录名
     * @return 目录内容的文件数组。
     * @since  0.1
     */
    public static File[] listAll(String fileName)
    {
        return listAll(new File(fileName));
    }

    /**
     * 列出目录中的所有内容，包括其子目录中的内容。
     * @param file 要列出的目录
     * @return 目录内容的文件数组。
     * @since  0.1
     */
    public static File[] listAll(File file)
    {
        ArrayList<File> list = new ArrayList<File>();
        File[] files;
        if (!file.exists() || file.isFile()) { return null; }
        list(list, file,  new FileFilter() {
            public boolean accept(File file) {
                //if the file extension is .txt return true, else false
            	  return true;
            }
			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return null;
			}
        });
        list.remove(file);
        files = new File[list.size()];
        list.toArray(files);
        return files;
    }

    /**
     * 列出目录中的所有内容，包括其子目录中的内容。
     * @param file 要列出的目录
     * @param filter 过滤器
     * @return 目录内容的文件数组。
     * @since  0.1
     */
    public static File[] listAll(File file,
            FileFilter filter)
    {
        ArrayList<File> list = new ArrayList<File>();
        File[] files;
        if (!file.exists() || file.isFile()) { return null; }
        list(list, file, filter);
        files = new File[list.size()];
        list.toArray(files);
        return files;
    }

    /**
     * 将目录中的内容添加到列表。
     * @param list 文件列表
     * @param filter 过滤器
     * @param file 目录
     */
    private static void list(ArrayList<File> list, File file,
            FileFilter filter)
    {
        if (filter.accept(file))
        {
            list.add(file);
            if (file.isFile()) { return; }
        }
        if (file.isDirectory())
        {
            File files[] = file.listFiles();
            for (int i = 0; i < files.length; i++)
            {
                list(list, files[i], filter);
            }
        }

    }

    /**
     * 返回文件的URL地址。
     * @param file 文件
     * @return 文件对应的的URL地址
     * @throws MalformedURLException
     * @since  0.4
     * @deprecated 在实现的时候没有注意到File类本身带一个toURL方法将文件路径转换为URL。
     *             请使用File.toURL方法。
     */
    public static URL getURL(File file) throws MalformedURLException
    {
        String fileURL = "file:/" + file.getAbsolutePath();
        URL url = new URL(fileURL);
        return url;
    }

    /**
     * 从文件路径得到文件名。
     * @param filePath 文件的路径，可以是相对路径也可以是绝对路径
     * @return 对应的文件名
     * @since  0.4
     */
    public static String getFileName(String filePath)
    {
        File file = new File(filePath);
        return file.getName();
    }

    /**
     * 从文件名得到文件绝对路径。
     * @param fileName 文件名
     * @return 对应的文件路径
     * @since  0.4
     */
    public static String getFilePath(String fileName)
    {
        File file = new File(fileName);
        return file.getAbsolutePath();
    }

    /**
     * 将DOS/Windows格式的路径转换为UNIX/Linux格式的路径。
     * 其实就是将路径中的"\"全部换为"/"，因为在某些情况下我们转换为这种方式比较方便，
     * 某中程度上说"/"比"\"更适合作为路径分隔符，而且DOS/Windows也将它当作路径分隔符。
     * @param filePath 转换前的路径
     * @return 转换后的路径
     * @since  0.4
     */
    public static String toUNIXpath(String filePath)
    {
        return filePath.replace('\\', '/');
    }

    /**
     * 从文件名得到UNIX风格的文件绝对路径。
     * @param fileName 文件名
     * @return 对应的UNIX风格的文件路径
     * @since  0.4
     * @see #toUNIXpath(String filePath) toUNIXpath
     */
    public static String getUNIXfilePath(String fileName)
    {
        File file = new File(fileName);
        return toUNIXpath(file.getAbsolutePath());
    }

    /**
     * 得到文件的类型。
     * 实际上就是得到文件名中最后一个“.”后面的部分。
     * @param fileName 文件名
     * @return 文件名中的类型部分
     * @since  0.5
     */
    public static String getTypePart(String fileName)
    {
        int point = fileName.lastIndexOf('.');
        int length = fileName.length();
        if (point == -1 || point == length - 1)
        {
            return "";
        }
        else
        {
            return fileName.substring(point + 1, length);
        }
    }

    /**
     * 得到文件的类型。
     * 实际上就是得到文件名中最后一个“.”后面的部分。
     * @param file 文件
     * @return 文件名中的类型部分
     * @since  0.5
     */
    public static String getFileType(File file)
    {
        return getTypePart(file.getName());
    }

    /**
     * 得到文件的名字部分。
     * 实际上就是路径中的最后一个路径分隔符后的部分。
     * @param fileName 文件名
     * @return 文件名中的名字部分
     * @since  0.5
     */
    public static String getNamePart(String fileName)
    {
        int point = getPathLastIndex(fileName);
        int length = fileName.length();
        if (point == -1)
        {
            return fileName;
        }
        else if (point == length - 1)
        {
            int secondPoint = getPathLastIndex(fileName, point - 1);
            if (secondPoint == -1)
            {
                if (length == 1)
                {
                    return fileName;
                }
                else
                {
                    return fileName.substring(0, point);
                }
            }
            else
            {
                return fileName.substring(secondPoint + 1, point);
            }
        }
        else
        {
            return fileName.substring(point + 1);
        }
    }

    /**
     * 得到文件名中的父路径部分。
     * 对两种路径分隔符都有效。
     * 不存在时返回""。
     * 如果文件名是以路径分隔符结尾的则不考虑该分隔符，例如"/path/"返回""。
     * @param fileName 文件名
     * @return 父路径，不存在或者已经是父目录时返回""
     * @since  0.5
     */
    public static String getPathPart(String fileName)
    {
        int point = getPathLastIndex(fileName);
        int length = fileName.length();
        if (point == -1)
        {
            return "";
        }
        else if (point == length - 1)
        {
            int secondPoint = getPathLastIndex(fileName, point - 1);
            if (secondPoint == -1)
            {
                return "";
            }
            else
            {
                return fileName.substring(0, secondPoint);
            }
        }
        else
        {
            return fileName.substring(0, point);
        }
    }

    /**
     * 得到路径分隔符在文件路径中首次出现的位置。
     * 对于DOS或者UNIX风格的分隔符都可以。
     * @param fileName 文件路径
     * @return 路径分隔符在路径中首次出现的位置，没有出现时返回-1。
     * @since  0.5
     */
    public static int getPathIndex(String fileName)
    {
        int point = fileName.indexOf('/');
        if (point == -1)
        {
            point = fileName.indexOf('\\');
        }
        return point;
    }

    /**
     * 得到路径分隔符在文件路径中指定位置后首次出现的位置。
     * 对于DOS或者UNIX风格的分隔符都可以。
     * @param fileName 文件路径
     * @param fromIndex 开始查找的位置
     * @return 路径分隔符在路径中指定位置后首次出现的位置，没有出现时返回-1。
     * @since  0.5
     */
    public static int getPathIndex(String fileName, int fromIndex)
    {
        int point = fileName.indexOf('/', fromIndex);
        if (point == -1)
        {
            point = fileName.indexOf('\\', fromIndex);
        }
        return point;
    }

    /**
     * 得到路径分隔符在文件路径中最后出现的位置。
     * 对于DOS或者UNIX风格的分隔符都可以。
     * @param fileName 文件路径
     * @return 路径分隔符在路径中最后出现的位置，没有出现时返回-1。
     * @since  0.5
     */
    public static int getPathLastIndex(String fileName)
    {
        int point = fileName.lastIndexOf('/');
        if (point == -1)
        {
            point = fileName.lastIndexOf('\\');
        }
        return point;
    }

    /**
     * 得到路径分隔符在文件路径中指定位置前最后出现的位置。
     * 对于DOS或者UNIX风格的分隔符都可以。
     * @param fileName 文件路径
     * @param fromIndex 开始查找的位置
     * @return 路径分隔符在路径中指定位置前最后出现的位置，没有出现时返回-1。
     * @since  0.5
     */
    public static int getPathLastIndex(String fileName, int fromIndex)
    {
        int point = fileName.lastIndexOf('/', fromIndex);
        if (point == -1)
        {
            point = fileName.lastIndexOf('\\', fromIndex);
        }
        return point;
    }

    /**
     * 将文件名中的类型部分去掉。
     * @param filename 文件名
     * @return 去掉类型部分的结果
     * @since  0.5
     */
    public static String trimType(String filename)
    {
        int index = filename.lastIndexOf(".");
        if (index != -1)
        {
            return filename.substring(0, index);
        }
        else
        {
            return filename;
        }
    }

    /**
     * 得到相对路径。
     * 文件名不是目录名的子节点时返回文件名。
     * @param pathName 目录名
     * @param fileName 文件名
     * @return 得到文件名相对于目录名的相对路径，目录下不存在该文件时返回文件名
     * @since  0.5
     */
    public static String getSubpath(String pathName, String fileName)
    {
        int index = fileName.indexOf(pathName);
        if (index != -1)
        {
            return fileName.substring(index + pathName.length() + 1);
        }
        else
        {
            return fileName;
        }
    }

    /**
     * 拷贝文件。
     * @param fromFileName 源文件名
     * @param toFileName 目标文件名
     * @return 成功生成文件时返回true，否则返回false
     * @since  0.6
     */
    public static boolean copy(String fromFileName, String toFileName)
    {
        return copy(fromFileName, toFileName, false);
    }

    /**
     * 拷贝文件。
     * @param fromFileName 源文件名
     * @param toFileName 目标文件名
     * @param override 目标文件存在时是否覆盖
     * @return 成功生成文件时返回true，否则返回false
     * @since  0.6
     */
    public static boolean copy(String fromFileName, String toFileName,
            boolean override)
    {
        File fromFile = new File(fromFileName);
        File toFile = new File(toFileName);

        if (!fromFile.exists() || !fromFile.isFile() || !fromFile.canRead()) { return false; }

        if (toFile.isDirectory())
        {
            toFile = new File(toFile, fromFile.getName());

        }
        if (toFile.exists())
        {
            if (!toFile.canWrite() || override == false) { return false; }
        }
        else
        {
            String parent = toFile.getParent();
            if (parent == null)
            {
                parent = System.getProperty("user.dir");
            }
            File dir = new File(parent);
            if (!dir.exists() || dir.isFile() || !dir.canWrite()) { return false; }
        }

        FileInputStream from = null;
        FileOutputStream to = null;
        try
        {
            from = new FileInputStream(fromFile);
            to = new FileOutputStream(toFile);
            byte[] buffer = new byte[4096];
            int bytes_read;
            while ((bytes_read = from.read(buffer)) != -1)
            {
                to.write(buffer, 0, bytes_read);
            }
            return true;
        }
        catch (IOException e)
        {
            return false;
        }
        finally
        {
            if (from != null)
            {
                try
                {
                    from.close();
                }
                catch (IOException e)
                {
                    ;
                }
            }
            if (to != null)
            {
                try
                {
                    to.close();
                }
                catch (IOException e)
                {
                    ;
                }
            }
        }
    }
    public static String getExtensionName(String filename) {
		return filename.substring(filename.lastIndexOf('.'));
	}
    	/**
    	 * 文件复制
    	 * @author yulong
    	 * @description
    	 * @date 2012-8-29 
    	 * @param src
    	 * @param dst
    	 * @param BUFFER_SIZE
    	 * @return
    	 */
 		public static boolean copy(File src, File dst, int BUFFER_SIZE) {
 			boolean result = false;
 			InputStream in = null;
 			OutputStream out = null;
 			try {
 				in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
 				out = new BufferedOutputStream(new FileOutputStream(dst),
 						BUFFER_SIZE);
 				byte[] buffer = new byte[BUFFER_SIZE];
 				int len = 0;
 				while ((len = in.read(buffer)) > 0) {
 					out.write(buffer, 0, len);
 				}
 				result = true;
 			} catch (Exception e) {
 				e.printStackTrace();
 				result = false;
 			} finally {
 				if (null != in) {
 					try {
 						in.close();
 					} catch (IOException e) {
 						e.printStackTrace();
 					}
 				}
 				if (null != out) {
 					try {
 						out.close();
 					} catch (IOException e) {
 						e.printStackTrace();
 					}
 				}
 			}
 			return result;
 		}
 		public static String getFileContents(File file) {
 			 StringBuilder result = new StringBuilder();
 	        try{
 	            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
 	            String s = null;
 	            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
 	                result.append(s).append("\n");
 	            }
 	            br.close();    
 	        }catch(Exception e){
 	            e.printStackTrace();
 	        }
 	        return result.toString();
 		}
 		
 		public static boolean copy(InputStream in, File dst, int BUFFER_SIZE) {
 			boolean result = false;
 			OutputStream out = null;
 			try {
 				out = new BufferedOutputStream(new FileOutputStream(dst),
 						BUFFER_SIZE);
 				byte[] buffer = new byte[BUFFER_SIZE];
 				int len = 0;
 				while ((len = in.read(buffer)) > 0) {
 					out.write(buffer, 0, len);
 				}
 				result = true;
 			} catch (Exception e) {
 				e.printStackTrace();
 				result = false;
 			} finally {
 				if (null != in) {
 					try {
 						in.close();
 					} catch (IOException e) {
 						e.printStackTrace();
 					}
 				}
 				if (null != out) {
 					try {
 						out.close();
 					} catch (IOException e) {
 						e.printStackTrace();
 					}
 				}
 			}
 			return result;
 		}
    /** 
     * 写文件到本地 
     * @param in 
     * @param fileName 
     * @throws IOException 
     */  
    public static void copyFile(InputStream in,String fileName,String basePath) throws IOException{
    	FileUtils.mkdir(basePath  
                  + fileName, true);
        FileOutputStream fs = new FileOutputStream(basePath  
                  + fileName);  
          byte[] buffer = new byte[1024 * 1024]; 
          int byteread = 0;  
          while ((byteread = in.read(buffer)) != -1) {  
              fs.write(buffer, 0, byteread);  
              fs.flush();  
          }  
          fs.close();  
          in.close();  
    }  
    // 建立文件夹
 	public void createFold(String path) {
 		try {
 			File folder = new File(path);
 			if (!folder.exists()) {
 				folder.mkdirs();
 			}
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 	}
    /**
     * 获得某个文件的绝对路径
     * @param x
     * @return
     */
    public static String getPath(String x)
    {
        String path = "";
        try
        {
            File file = new File(".");
            path = file.getCanonicalPath() + "\\" + x + "\\";
        }
        catch (IOException e)
        {
            System.out.println("com.job5156.util.FileUtil getPath()  error :"
                    + e.getMessage());
        }
        return path;
    }
    
    public static boolean existFile(String flePath){
    
		return existFile(new File(flePath));		
    }
    
    public static boolean existFile(File file){
    	if(file == null)
    		return false;
    	try {
			return file.exists();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    }

    /**
     * 获得路径
     * @return  根目录，java/jsp应用程序的根目录
     * @author Lzj.Liu
     */
    public static String getPath()
    {
        String path = "";
        File file = new File(".");
        try
        {
            path = file.getCanonicalPath();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return path;
    }

    /**
     * 创建资料夹(测试通过)
     * @param folderName 文件夹名称
     * @return boolean true:ok;false:faild
     */
    public static boolean createFolder(String folderName)
    {
        boolean bTmp = false;
        try
        {
            folderName = folderName.toString();
            File file = new File(folderName);
            if(!file.exists()){
	            file.mkdirs();
            }
            bTmp = true;
        }
        catch (Exception ex)
        {
            System.out.println(ex.toString());
            bTmp = false;
        }
        return bTmp;
    }

    /**
     * 删除指定的文件
     * @param fileName 指定的文件路径及文件名
     * @return boolean true:成功;false:失败
     */
    public static boolean deleteFile(String fileName)
    {
        boolean bTmp = false;
        try
        {
            fileName = fileName.toString();
            File file = new File(fileName);
            if (file.exists())
            {
                file.delete();
                bTmp = true;
            }
            else
            {
                System.out.println(fileName + "文件路径有误!");
                bTmp = false;
            }
        }
        catch (Exception ex)
        {
            bTmp = false;
            System.out.println("FileUtil->deleteFile" + ex.toString());
        }
        return bTmp;
    }

    /**
     * 创建指定的文件
     * @param fileName 文件路径及文件名(如:c:\\a.txt);
     * @return boolean true:成功;false:失败
     */
    public static boolean createFile(String filePath)
    {
        boolean bTmp = false;
        filePath = filePath.toString();
        File myFilePath = new File(filePath);
        FileWriter resultFile = null;
        try
        {
            if (!myFilePath.exists()) myFilePath.createNewFile();
            resultFile = new FileWriter(myFilePath);
            /*PrintWriter myFile = */new PrintWriter(resultFile);
        }
        catch (Exception ex)
        {
            System.out.println(ex.toString());
        }
        finally
        {
            try
            {
                resultFile.close();
            }
            catch (IOException ioe)
            {
                System.out.println(ioe.toString());
            }
        }
        return bTmp;
    }


    /**
     * 获得路径
     * @return 根目录
     */
    public static String getRootPath()
    {
        return getPath();
    }

    public static void main(String[] args)
    {
        //boolean b=createFolder("uploadpic\\2002515\\xxx.gif");
        //System.out.println(b);
        //boolean bTmp=isFileExist("WEB-INF\\src\\com\\job5156\\util\\FileUtil.java");
        //System.out.println(bTmp);
        //System.out.println(getPath());
        //makeDirectory("uploadpic\\200515\\xxxzzz.gif");
        //deleteDirectory("uploadpic\\200515\\xxxzzz.gif");
        //copy("uploadpic\\123.gif","uploadpic\\xxxyy.gif\\xxx.gif",true);
        //emptyDirectory("uploadpic\\200515");
        System.out.println("---:"+FileUtils.getFileContents(new File("/Users/yulong/git/myutils/src/main/java/com/yulon/util/DesUtil.java")));
    }
}
