package com.example.zjq.mobileplayer.utils;


import com.example.zjq.mobileplayer.bean.Lyric;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * 解析歌词工具类
 */
public class LyricUtils {


    /**
     * 得到解析好的歌词列表
     * @return
     */
    public ArrayList<Lyric> getLyrics() {
        return lyrics;
    }

    private ArrayList<Lyric> lyrics;



    private boolean isExistsLyric=false;

    //是否存在歌词
    public boolean isExistsLyric() {
        return isExistsLyric;
    }

    /**
     * 读取歌词文件
     *
     * @param file
     */
    public void readLyricFile(File file) {

        if (file == null || !file.exists()) {
            //歌词文件不存在
            lyrics = null;

            isExistsLyric=false;
        } else {
            //歌词文件存在
            //1.解析歌词-一行一行的读取-再解析
            lyrics = new ArrayList<>();
            isExistsLyric=true;
            BufferedReader reader = null;
            try {

                reader = new BufferedReader( new InputStreamReader( new FileInputStream( file ), getCharset( file ) ) );

                String line = "";
                while ((line = reader.readLine()) != null) {

                    line = parsedLyric( line );
                }

                reader.close();


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //2.排序

            Collections.sort( lyrics, new Comparator<Lyric>() {
                @Override
                public int compare(Lyric o1, Lyric o2) {

                    if (o1.getTimePoint()<o2.getTimePoint()){
                        return -1;
                    }else if (o1.getTimePoint()>o2.getTimePoint()){
                        return 1;
                    }else {
                        return 0;
                    }

                }
            } );

            //3.计算每句高亮
            for (int i=0;i<lyrics.size();i++){
                Lyric oneLyrc=lyrics.get( i );
                if (i+1<lyrics.size()){
                    Lyric twoLyric=lyrics.get( i+1 );
                    oneLyrc.setSleepTime( twoLyric.getTimePoint()-oneLyrc.getTimePoint() );
                }
            }
        }


    }

    /**
     * 解析一句歌词
     *
     * @param line
     * @return
     */
    private String parsedLyric(String line) {

        //indexOf第一次出现这个得位置；有返回位置，没有返回-1
        int pos1 = line.indexOf( "[" );//0
        int pos2 = line.indexOf( "]" );//9

        if (pos1 == 0 && pos2 != -1) {//肯定有歌词

            //装时间
            long[] times = new long[getCountTag( line )];

            String strTime = line.substring( pos1 + 1, pos2 ); //把[02:04.12] 转化出时间 02:04.12
            times[0] = strTime2LongTime( strTime );


            String content = line;
            int i = 1;
            while (pos1 == 0 && pos2 != -1) {

                content = content.substring( pos2 + 1 );
                pos1 = content.indexOf( "[" );
                pos2 = content.indexOf( "]" );

                if (pos2 != -1) {
                    strTime = content.substring( pos1 = 1, pos2 );
                    times[i] = strTime2LongTime( strTime );

                    if (times[i] == -1) {
                        return "";
                    }

                    i++;
                }


            }

            //把时间数组和文本关联起来，并且加入到集合中
            for (int j=0;j<times.length;j++){
                if (times[j]!=0){//有时间戳

                    Lyric lyric=new Lyric();

                    lyric.setContent( content );
                    lyric.setTimePoint( times[j] );

                    //添加到集合中
                    lyrics.add( lyric );
                }
            }

            return content;//返回文本
        }
        return null;
    }

    /**
     * 把String类型的时间转换long类型
     *
     * @param strTime
     * @return
     */
    private long strTime2LongTime(String strTime) {
        long result = -1;


        try {

            //1.把02:04.12按照：切割成02和04.12
            String[] s1 = strTime.split( ":" );

            //2.把04.12按照.切割成04和12
            String[] s2 = s1[1].split( "\\." );

            //1.分

            long min = Long.parseLong( s1[0] );
            //2.秒

            long second = Long.parseLong( s2[0] );

            //3.毫秒

            long mil = Long.parseLong( s2[1] );

            result = min * 60 * 1000 + second * 1000 + mil * 10;
        } catch (Exception e) {

            e.printStackTrace();
            result = -1;
        }

        return result;
    }

    /**
     * 判断有多少句歌词
     *
     * @param line
     * @return
     */
    private int getCountTag(String line) {

        int result = -1;

        String[] left = line.split( "\\[" );
        String[] right = line.split( "\\]" );

        if (left.length == 0 && right.length == 0) {
            return 1;
        } else if (left.length > right.length) {
            result = left.length;
        } else {
            result = right.length;
        }
        return result;
    }


    /**
     * 判断文件编码
     * @param file 文件
     * @return 编码：GBK,UTF-8,UTF-16LE
     */
    public String getCharset(File file) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(file));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1)
                return charset;
            if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF
                    && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8";
                checked = true;
            }
            bis.reset();
            if (!checked) {
                int loc = 0;
                while ((read = bis.read()) != -1) {
                    loc++;
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF)
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF)
                            continue;
                        else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
            }
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return charset;
    }

}
