package com.example.zjq.mobileplayer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 网络音乐数据
 */
public class NetAudioPagerData implements Serializable {



    private InfoBean info;
    private List<ListBean> list;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class InfoBean {


        private int count;
        private int np;

        /*public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getNp() {
            return np;
        }

        public void setNp(int np) {
            this.np = np;
        }*/
    }

    public static class ListBean {


        private int status;
        private String rating;
        private String cate;
        private int is_bookmark;
        private String text;
        private int is_best;
        private int video_signs;
        private String share_url;
        private int up;
        private int down;
        private int forward;
        private UBean u;
        private int bookmark;
        private String passtime;
        private VideoBean video;
        private String type;
        private String id;
        private String comment;
        private GifBean gif;
        private ImageBean image;
        private List<TagsBean> tags;
        private List<TopCommentsBean> top_comments;

      /*  public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getCate() {
            return cate;
        }

        public void setCate(String cate) {
            this.cate = cate;
        }

        public int getIs_bookmark() {
            return is_bookmark;
        }

        public void setIs_bookmark(int is_bookmark) {
            this.is_bookmark = is_bookmark;
        }*/

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
/*
        public int getIs_best() {
            return is_best;
        }

        public void setIs_best(int is_best) {
            this.is_best = is_best;
        }

        public int getVideo_signs() {
            return video_signs;
        }

        public void setVideo_signs(int video_signs) {
            this.video_signs = video_signs;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }*/

        public int getUp() {
            return up;
        }

        public void setUp(int up) {
            this.up = up;
        }

      /*  public int getDown() {
            return down;
        }

        public void setDown(int down) {
            this.down = down;
        }

        public int getForward() {
            return forward;
        }

        public void setForward(int forward) {
            this.forward = forward;
        }*/

        public UBean getU() {
            return u;
        }

      /*  public void setU(UBean u) {
            this.u = u;
        }

        public int getBookmark() {
            return bookmark;
        }

        public void setBookmark(int bookmark) {
            this.bookmark = bookmark;
        }*/

        public String getPasstime() {
            return passtime;
        }

     /*   public void setPasstime(String passtime) {
            this.passtime = passtime;
        }*/

        public VideoBean getVideo() {
            return video;
        }

        /*public void setVideo(VideoBean video) {
            this.video = video;
        }*/

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

       /* public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }*/

        public GifBean getGif() {
            return gif;
        }

        /*public void setGif(GifBean gif) {
            this.gif = gif;
        }*/

        public ImageBean getImage() {
            return image;
        }

        public void setImage(ImageBean image) {
            this.image = image;
        }

        /*public List<TagsBean> getTags() {
            return tags;
        }

        public void setTags(List<TagsBean> tags) {
            this.tags = tags;
        }

        public List<TopCommentsBean> getTop_comments() {
            return top_comments;
        }

        public void setTop_comments(List<TopCommentsBean> top_comments) {
            this.top_comments = top_comments;
        }*/

        public static class UBean {


            private int relationship;
            private String uid;
            private boolean is_vip;
            private boolean is_v;
            private String room_url;
            private String room_name;
            private String room_role;
            private String room_icon;
            private String name;
            private List<String> header;

           /* public int getRelationship() {
                return relationship;
            }

            public void setRelationship(int relationship) {
                this.relationship = relationship;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public boolean isIs_vip() {
                return is_vip;
            }

            public void setIs_vip(boolean is_vip) {
                this.is_vip = is_vip;
            }

            public boolean isIs_v() {
                return is_v;
            }

            public void setIs_v(boolean is_v) {
                this.is_v = is_v;
            }

            public String getRoom_url() {
                return room_url;
            }

            public void setRoom_url(String room_url) {
                this.room_url = room_url;
            }

            public String getRoom_name() {
                return room_name;
            }

            public void setRoom_name(String room_name) {
                this.room_name = room_name;
            }

            public String getRoom_role() {
                return room_role;
            }

            public void setRoom_role(String room_role) {
                this.room_role = room_role;
            }

            public String getRoom_icon() {
                return room_icon;
            }

            public void setRoom_icon(String room_icon) {
                this.room_icon = room_icon;
            }*/

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<String> getHeader() {
                return header;
            }

          /*  public void setHeader(List<String> header) {
                this.header = header;
            }*/
        }

        public static class VideoBean {


            private int thumbnail_height;
            private int long_picture;
            private int playfcount;
            private int thumbnail_width;
            private int height;
            private int width;
            private int duration;
            private int playcount;
            private List<String> thumbnail_link;
            private List<String> video;
            private List<String> download;
            private List<String> thumbnail;
            private List<String> thumbnail_small;

            public int getThumbnail_height() {
                return thumbnail_height;
            }

            public void setThumbnail_height(int thumbnail_height) {
                this.thumbnail_height = thumbnail_height;
            }

            public int getLong_picture() {
                return long_picture;
            }

            public void setLong_picture(int long_picture) {
                this.long_picture = long_picture;
            }

            public int getPlayfcount() {
                return playfcount;
            }

            public void setPlayfcount(int playfcount) {
                this.playfcount = playfcount;
            }

            public int getThumbnail_width() {
                return thumbnail_width;
            }

            public void setThumbnail_width(int thumbnail_width) {
                this.thumbnail_width = thumbnail_width;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getDuration() {
                return duration;
            }

            public void setDuration(int duration) {
                this.duration = duration;
            }

            public int getPlaycount() {
                return playcount;
            }

            public void setPlaycount(int playcount) {
                this.playcount = playcount;
            }

            public List<String> getThumbnail_link() {
                return thumbnail_link;
            }

            public void setThumbnail_link(List<String> thumbnail_link) {
                this.thumbnail_link = thumbnail_link;
            }

            public List<String> getVideo() {
                return video;
            }

            public void setVideo(List<String> video) {
                this.video = video;
            }

            public List<String> getDownload() {
                return download;
            }

            public void setDownload(List<String> download) {
                this.download = download;
            }

            public List<String> getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(List<String> thumbnail) {
                this.thumbnail = thumbnail;
            }

            public List<String> getThumbnail_small() {
                return thumbnail_small;
            }

            public void setThumbnail_small(List<String> thumbnail_small) {
                this.thumbnail_small = thumbnail_small;
            }
        }

        public static class GifBean {
            /**
             * long_picture : 0
             * gif_thumbnail : ["http://wimg.spriteapp.cn/ugc/2019/08/20/5d5bf79f3f0e0_a_1.jpg","http://dimg.spriteapp.cn/ugc/2019/08/20/5d5bf79f3f0e0_a_1.jpg"]
             * thumbnail_link : ["http://wimg.spriteapp.cn/cropx/533x241/ugc/2019/08/20/5d5bf79f3f0e0_a_1.jpg","http://dimg.spriteapp.cn/cropx/533x241/ugc/2019/08/20/5d5bf79f3f0e0_a_1.jpg"]
             * download_url : ["http://wimg.spriteapp.cn/ugc/2019/08/20/5d5bf79f3f0e0_d.jpg","http://dimg.spriteapp.cn/ugc/2019/08/20/5d5bf79f3f0e0_d.jpg","http://wimg.spriteapp.cn/ugc/2019/08/20/5d5bf79f3f0e0_a_1.jpg","http://dimg.spriteapp.cn/ugc/2019/08/20/5d5bf79f3f0e0_a_1.jpg"]
             * height : 241
             * width : 533
             * thumbnail_width : 533
             * images : ["http://wimg.spriteapp.cn/ugc/2019/08/20/5d5bf79f3f0e0.gif","http://dimg.spriteapp.cn/ugc/2019/08/20/5d5bf79f3f0e0.gif"]
             * thumbnail_height : 241
             */

            private int long_picture;
            private int height;
            private int width;
            private int thumbnail_width;
            private int thumbnail_height;
            private List<String> gif_thumbnail;
            private List<String> thumbnail_link;
            private List<String> download_url;
            private List<String> images;

            public int getLong_picture() {
                return long_picture;
            }

            public void setLong_picture(int long_picture) {
                this.long_picture = long_picture;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getThumbnail_width() {
                return thumbnail_width;
            }

            public void setThumbnail_width(int thumbnail_width) {
                this.thumbnail_width = thumbnail_width;
            }

            public int getThumbnail_height() {
                return thumbnail_height;
            }

            public void setThumbnail_height(int thumbnail_height) {
                this.thumbnail_height = thumbnail_height;
            }

            public List<String> getGif_thumbnail() {
                return gif_thumbnail;
            }

            public void setGif_thumbnail(List<String> gif_thumbnail) {
                this.gif_thumbnail = gif_thumbnail;
            }

            public List<String> getThumbnail_link() {
                return thumbnail_link;
            }

            public void setThumbnail_link(List<String> thumbnail_link) {
                this.thumbnail_link = thumbnail_link;
            }

            public List<String> getDownload_url() {
                return download_url;
            }

            public void setDownload_url(List<String> download_url) {
                this.download_url = download_url;
            }

            public List<String> getImages() {
                return images;
            }

            public void setImages(List<String> images) {
                this.images = images;
            }
        }

        public static class ImageBean {


            private int long_picture;
            private int height;
            private int width;
            private int thumbnail_width;
            private int thumbnail_height;
            private List<?> medium;
            private List<String> thumbnail_link;
            private List<String> big;
            private List<String> download_url;
            private List<?> small;
            private List<String> thumbnail_picture;
            private List<String> thumbnail_small;

            public int getLong_picture() {
                return long_picture;
            }

            public void setLong_picture(int long_picture) {
                this.long_picture = long_picture;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getThumbnail_width() {
                return thumbnail_width;
            }

            public void setThumbnail_width(int thumbnail_width) {
                this.thumbnail_width = thumbnail_width;
            }

            public int getThumbnail_height() {
                return thumbnail_height;
            }

            public void setThumbnail_height(int thumbnail_height) {
                this.thumbnail_height = thumbnail_height;
            }

            public List<?> getMedium() {
                return medium;
            }

            public void setMedium(List<?> medium) {
                this.medium = medium;
            }

            public List<String> getThumbnail_link() {
                return thumbnail_link;
            }

            public void setThumbnail_link(List<String> thumbnail_link) {
                this.thumbnail_link = thumbnail_link;
            }

            public List<String> getBig() {
                return big;
            }

            public void setBig(List<String> big) {
                this.big = big;
            }

            public List<String> getDownload_url() {
                return download_url;
            }

            public void setDownload_url(List<String> download_url) {
                this.download_url = download_url;
            }

            public List<?> getSmall() {
                return small;
            }

            public void setSmall(List<?> small) {
                this.small = small;
            }

            public List<String> getThumbnail_picture() {
                return thumbnail_picture;
            }

            public void setThumbnail_picture(List<String> thumbnail_picture) {
                this.thumbnail_picture = thumbnail_picture;
            }

            public List<String> getThumbnail_small() {
                return thumbnail_small;
            }

            public void setThumbnail_small(List<String> thumbnail_small) {
                this.thumbnail_small = thumbnail_small;
            }
        }

        public static class TagsBean {
            /**
             * post_number : 65484
             * image_list : http://img.spriteapp.cn/ugc/2018/05/0215c87a598011e8af20842b2b4c75ab.png
             * forum_sort : 0
             * forum_status : 2
             * id : 60369
             * info : 想看女神萌妹嘛？来本板块是你最明智的选择！
             * 本板块内直接收【女神】&【萌妹子】哦~~
             * 请各位姐夫积极维护本板块~
             * 并一起打造一个完美的国度吧~
             * name : 女神萌妹
             * colum_set : 2
             * tail : 个萌妹子
             * sub_number : 42638
             * display_level : 0
             */

            private int post_number;
            private String image_list;
            private int forum_sort;
            private int forum_status;
            private int id;
            private String info;
            private String name;
            private int colum_set;
            private String tail;
            private int sub_number;
            private int display_level;

            public int getPost_number() {
                return post_number;
            }

            public void setPost_number(int post_number) {
                this.post_number = post_number;
            }

            public String getImage_list() {
                return image_list;
            }

            public void setImage_list(String image_list) {
                this.image_list = image_list;
            }

            public int getForum_sort() {
                return forum_sort;
            }

            public void setForum_sort(int forum_sort) {
                this.forum_sort = forum_sort;
            }

            public int getForum_status() {
                return forum_status;
            }

            public void setForum_status(int forum_status) {
                this.forum_status = forum_status;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getInfo() {
                return info;
            }

            public void setInfo(String info) {
                this.info = info;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getColum_set() {
                return colum_set;
            }

            public void setColum_set(int colum_set) {
                this.colum_set = colum_set;
            }

            public String getTail() {
                return tail;
            }

            public void setTail(String tail) {
                this.tail = tail;
            }

            public int getSub_number() {
                return sub_number;
            }

            public void setSub_number(int sub_number) {
                this.sub_number = sub_number;
            }

            public int getDisplay_level() {
                return display_level;
            }

            public void setDisplay_level(int display_level) {
                this.display_level = display_level;
            }
        }

        public static class TopCommentsBean {
            /**
             * voicetime : 0
             * status : 0
             * hate_count : 0
             * cmt_type : text
             * precid : 0
             * content : 不老是种祝福，不死是一种诅咒，
             * like_count : 67
             * u : {"header":["http://wimg.spriteapp.cn/profile","http://dimg.spriteapp.cn/profile"],"uid":"17200739","is_vip":false,"room_url":"","sex":"m","room_name":"","room_role":"","room_icon":"","name":"百思大才子"}
             * preuid : 0
             * passtime : 2019-08-20 12:37:10
             * voiceuri :
             * id : 15711291
             */

            private int voicetime;
            private int status;
            private int hate_count;
            private String cmt_type;
            private int precid;
            private String content;
            private int like_count;
            private UBeanX u;
            private int preuid;
            private String passtime;
            private String voiceuri;
            private int id;

            public int getVoicetime() {
                return voicetime;
            }

            public void setVoicetime(int voicetime) {
                this.voicetime = voicetime;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getHate_count() {
                return hate_count;
            }

            public void setHate_count(int hate_count) {
                this.hate_count = hate_count;
            }

            public String getCmt_type() {
                return cmt_type;
            }

            public void setCmt_type(String cmt_type) {
                this.cmt_type = cmt_type;
            }

            public int getPrecid() {
                return precid;
            }

            public void setPrecid(int precid) {
                this.precid = precid;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getLike_count() {
                return like_count;
            }

            public void setLike_count(int like_count) {
                this.like_count = like_count;
            }

            public UBeanX getU() {
                return u;
            }

            public void setU(UBeanX u) {
                this.u = u;
            }

            public int getPreuid() {
                return preuid;
            }

            public void setPreuid(int preuid) {
                this.preuid = preuid;
            }

            public String getPasstime() {
                return passtime;
            }

            public void setPasstime(String passtime) {
                this.passtime = passtime;
            }

            public String getVoiceuri() {
                return voiceuri;
            }

            public void setVoiceuri(String voiceuri) {
                this.voiceuri = voiceuri;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public static class UBeanX {
                /**
                 * header : ["http://wimg.spriteapp.cn/profile","http://dimg.spriteapp.cn/profile"]
                 * uid : 17200739
                 * is_vip : false
                 * room_url :
                 * sex : m
                 * room_name :
                 * room_role :
                 * room_icon :
                 * name : 百思大才子
                 */

                private String uid;
                private boolean is_vip;
                private String room_url;
                private String sex;
                private String room_name;
                private String room_role;
                private String room_icon;
                private String name;
                private List<String> header;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

                public boolean isIs_vip() {
                    return is_vip;
                }

                public void setIs_vip(boolean is_vip) {
                    this.is_vip = is_vip;
                }

                public String getRoom_url() {
                    return room_url;
                }

                public void setRoom_url(String room_url) {
                    this.room_url = room_url;
                }

                public String getSex() {
                    return sex;
                }

                public void setSex(String sex) {
                    this.sex = sex;
                }

                public String getRoom_name() {
                    return room_name;
                }

                public void setRoom_name(String room_name) {
                    this.room_name = room_name;
                }

                public String getRoom_role() {
                    return room_role;
                }

                public void setRoom_role(String room_role) {
                    this.room_role = room_role;
                }

                public String getRoom_icon() {
                    return room_icon;
                }

                public void setRoom_icon(String room_icon) {
                    this.room_icon = room_icon;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public List<String> getHeader() {
                    return header;
                }

                public void setHeader(List<String> header) {
                    this.header = header;
                }
            }
        }
    }
}
