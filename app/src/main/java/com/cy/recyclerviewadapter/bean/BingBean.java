package com.cy.recyclerviewadapter.bean;

import java.util.List;

/**
 * @Description:
 * @Author: cy
 * @CreateDate: 2020/6/10 18:18
 * @UpdateUser:
 * @UpdateDate: 2020/6/10 18:18
 * @UpdateRemark:
 * @Version:
 */
public class BingBean {

    /**
     * images : [{"startdate":"20200609","fullstartdate":"202006091600","enddate":"20200610","url":"/th?id=OHR.WobblyBridge_ZH-CN7751845685_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp","urlbase":"/th?id=OHR.WobblyBridge_ZH-CN7751845685","copyright":"以圣保罗大教堂为背景的千禧桥，英国伦敦 (© Scott Baldock/Getty Images)","copyrightlink":"https://www.bing.com/search?q=%E5%8D%83%E7%A6%A7%E6%A1%A5&form=hpcapt&mkt=zh-cn","title":"","quiz":"/search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20200609_WobblyBridge%22&FORM=HPQUIZ","wp":true,"hsh":"c87981898ffde2a847be219d1f53af7b","drk":1,"top":1,"bot":1,"hs":[]},{"startdate":"20200608","fullstartdate":"202006081600","enddate":"20200609","url":"/th?id=OHR.BaronLakes_ZH-CN7541190370_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp","urlbase":"/th?id=OHR.BaronLakes_ZH-CN7541190370","copyright":"锯齿原野中的Baron湖，爱达荷州 (© Patrick Brandenburg/Tandem Stills + Motion)","copyrightlink":"https://www.bing.com/search?q=%E9%94%AF%E9%BD%BF%E5%8E%9F%E9%87%8E&form=hpcapt&mkt=zh-cn","title":"","quiz":"/search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20200608_BaronLakes%22&FORM=HPQUIZ","wp":true,"hsh":"09b76ae4612eabb445fc330a5dff5a34","drk":1,"top":1,"bot":1,"hs":[]},{"startdate":"20200607","fullstartdate":"202006071600","enddate":"20200608","url":"/th?id=OHR.LionSurfing_ZH-CN7369892268_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp","urlbase":"/th?id=OHR.LionSurfing_ZH-CN7369892268","copyright":"费尔南迪纳岛海岸的加拉帕戈斯海狮，厄瓜多尔科隆群岛 (© Tui De Roy/Minden Pictures)","copyrightlink":"https://www.bing.com/search?q=%E5%8A%A0%E6%8B%89%E5%B8%95%E6%88%88%E6%96%AF%E6%B5%B7%E7%8B%AE&form=hpcapt&mkt=zh-cn","title":"","quiz":"/search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20200607_LionSurfing%22&FORM=HPQUIZ","wp":true,"hsh":"122f76fbf1e01da17572a638cacb4b43","drk":1,"top":1,"bot":1,"hs":[]},{"startdate":"20200606","fullstartdate":"202006061600","enddate":"20200607","url":"/th?id=OHR.LaPertusa_ZH-CN7227946330_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp","urlbase":"/th?id=OHR.LaPertusa_ZH-CN7227946330","copyright":"La Pertusa教堂，西班牙莱里达 (© bbsferrari/Getty Images)","copyrightlink":"https://www.bing.com/search?q=La+Pertusa%E6%95%99%E5%A0%82&form=hpcapt&mkt=zh-cn","title":"","quiz":"/search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20200606_LaPertusa%22&FORM=HPQUIZ","wp":true,"hsh":"87435025539ec2be3e5f1f10d903f269","drk":1,"top":1,"bot":1,"hs":[]},{"startdate":"20200605","fullstartdate":"202006051600","enddate":"20200606","url":"/th?id=OHR.WaltersWiggles_ZH-CN6928617440_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp","urlbase":"/th?id=OHR.WaltersWiggles_ZH-CN6928617440","copyright":"锡安国家公园中的Walter's Wiggles小径，犹他州 (© Dennis Frates/Alamy)","copyrightlink":"https://www.bing.com/search?q=%E9%94%A1%E5%AE%89%E5%9B%BD%E5%AE%B6%E5%85%AC%E5%9B%AD&form=hpcapt&mkt=zh-cn","title":"","quiz":"/search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20200605_WaltersWiggles%22&FORM=HPQUIZ","wp":true,"hsh":"6082b91a7b5ebc37e8278028deb19157","drk":1,"top":1,"bot":1,"hs":[]},{"startdate":"20200604","fullstartdate":"202006041600","enddate":"20200605","url":"/th?id=OHR.SynchronousFireflies_ZH-CN6323931412_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp","urlbase":"/th?id=OHR.SynchronousFireflies_ZH-CN6323931412","copyright":"同步发光萤火虫照亮了大烟山国家公园中的森林 (© Floris van Breugel/Minden Pictures)","copyrightlink":"https://www.bing.com/search?q=%E5%A4%A7%E7%83%9F%E5%B1%B1%E5%9B%BD%E5%AE%B6%E5%85%AC%E5%9B%AD&form=hpcapt&mkt=zh-cn","title":"","quiz":"/search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20200604_SynchronousFireflies%22&FORM=HPQUIZ","wp":true,"hsh":"005e5ca46a34af5af055e70c12873f60","drk":1,"top":1,"bot":1,"hs":[]},{"startdate":"20200603","fullstartdate":"202006031600","enddate":"20200604","url":"/th?id=OHR.PontFawr_ZH-CN1780190468_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp","urlbase":"/th?id=OHR.PontFawr_ZH-CN1780190468","copyright":"兰鲁斯特一座名为Pont Fawr的石拱桥，英国威尔士 (© Pajor Pawel/Shutterstock)","copyrightlink":"https://www.bing.com/search?q=Pont+Fawr&form=hpcapt&mkt=zh-cn","title":"","quiz":"/search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20200603_PontFawr%22&FORM=HPQUIZ","wp":true,"hsh":"81964e143bb9618a52e6aca6a17623cd","drk":1,"top":1,"bot":1,"hs":[]},{"startdate":"20200602","fullstartdate":"202006021600","enddate":"20200603","url":"/th?id=OHR.WhiteRimTrail_ZH-CN1574735777_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp","urlbase":"/th?id=OHR.WhiteRimTrail_ZH-CN1574735777","copyright":"两名山地车骑手在白缘公路上沿着谢福小道的转弯处骑行 ，犹他州峡谷地国家公园 (© Grant Ordelheide/Tandem Stills + Motion)","copyrightlink":"https://www.bing.com/search?q=%E5%B3%A1%E8%B0%B7%E5%9C%B0%E5%9B%BD%E5%AE%B6%E5%85%AC%E5%9B%AD&form=hpcapt&mkt=zh-cn","title":"","quiz":"/search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20200602_WhiteRimTrail%22&FORM=HPQUIZ","wp":true,"hsh":"e6a819d5c93d9a1ca1158e442a6e2e36","drk":1,"top":1,"bot":1,"hs":[]}]
     * tooltips : {"loading":"正在加载...","previous":"上一个图像","next":"下一个图像","walle":"此图片不能下载用作壁纸。","walls":"下载今日美图。仅限用作桌面壁纸。"}
     */

    private TooltipsBean tooltips;
    private List<ImagesBean> images;

    public TooltipsBean getTooltips() {
        return tooltips;
    }

    public void setTooltips(TooltipsBean tooltips) {
        this.tooltips = tooltips;
    }

    public List<ImagesBean> getImages() {
        return images;
    }

    public void setImages(List<ImagesBean> images) {
        this.images = images;
    }

    public static class TooltipsBean {
        /**
         * loading : 正在加载...
         * previous : 上一个图像
         * next : 下一个图像
         * walle : 此图片不能下载用作壁纸。
         * walls : 下载今日美图。仅限用作桌面壁纸。
         */

        private String loading;
        private String previous;
        private String next;
        private String walle;
        private String walls;

        public String getLoading() {
            return loading;
        }

        public void setLoading(String loading) {
            this.loading = loading;
        }

        public String getPrevious() {
            return previous;
        }

        public void setPrevious(String previous) {
            this.previous = previous;
        }

        public String getNext() {
            return next;
        }

        public void setNext(String next) {
            this.next = next;
        }

        public String getWalle() {
            return walle;
        }

        public void setWalle(String walle) {
            this.walle = walle;
        }

        public String getWalls() {
            return walls;
        }

        public void setWalls(String walls) {
            this.walls = walls;
        }
    }

    public static class ImagesBean {
        /**
         * startdate : 20200609
         * fullstartdate : 202006091600
         * enddate : 20200610
         * url : /th?id=OHR.WobblyBridge_ZH-CN7751845685_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp
         * urlbase : /th?id=OHR.WobblyBridge_ZH-CN7751845685
         * copyright : 以圣保罗大教堂为背景的千禧桥，英国伦敦 (© Scott Baldock/Getty Images)
         * copyrightlink : https://www.bing.com/search?q=%E5%8D%83%E7%A6%A7%E6%A1%A5&form=hpcapt&mkt=zh-cn
         * title :
         * quiz : /search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20200609_WobblyBridge%22&FORM=HPQUIZ
         * wp : true
         * hsh : c87981898ffde2a847be219d1f53af7b
         * drk : 1
         * top : 1
         * bot : 1
         * hs : []
         */

        private String startdate;
        private String fullstartdate;
        private String enddate;
        private String url;
        private String urlbase;
        private String copyright;
        private String copyrightlink;
        private String title;
        private String quiz;
        private boolean wp;
        private String hsh;
        private int drk;
        private int top;
        private int bot;
        private List<?> hs;

        public String getStartdate() {
            return startdate;
        }

        public void setStartdate(String startdate) {
            this.startdate = startdate;
        }

        public String getFullstartdate() {
            return fullstartdate;
        }

        public void setFullstartdate(String fullstartdate) {
            this.fullstartdate = fullstartdate;
        }

        public String getEnddate() {
            return enddate;
        }

        public void setEnddate(String enddate) {
            this.enddate = enddate;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrlbase() {
            return urlbase;
        }

        public void setUrlbase(String urlbase) {
            this.urlbase = urlbase;
        }

        public String getCopyright() {
            return copyright;
        }

        public void setCopyright(String copyright) {
            this.copyright = copyright;
        }

        public String getCopyrightlink() {
            return copyrightlink;
        }

        public void setCopyrightlink(String copyrightlink) {
            this.copyrightlink = copyrightlink;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getQuiz() {
            return quiz;
        }

        public void setQuiz(String quiz) {
            this.quiz = quiz;
        }

        public boolean isWp() {
            return wp;
        }

        public void setWp(boolean wp) {
            this.wp = wp;
        }

        public String getHsh() {
            return hsh;
        }

        public void setHsh(String hsh) {
            this.hsh = hsh;
        }

        public int getDrk() {
            return drk;
        }

        public void setDrk(int drk) {
            this.drk = drk;
        }

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
        }

        public int getBot() {
            return bot;
        }

        public void setBot(int bot) {
            this.bot = bot;
        }

        public List<?> getHs() {
            return hs;
        }

        public void setHs(List<?> hs) {
            this.hs = hs;
        }
    }
}
