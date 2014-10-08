<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/tld/taglibs.jsp" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<style>
        /*全局定义 www.afeidy.com-2013-05-03*/
    * {
        padding: 0;
        margin: 0;
    }

    .hdwrap {
        width: ${param.width}px;
        margin: 0 auto;
    }

        /*首页图片滚动*/

    .flashlist {
        height: ${param.height}px;
    }

    .flashlist .f_out {
        height: ${param.height}px;
        overflow: hidden;
        position: relative;
        z-index: 100;
    }

    .flashlist .f_out .vip01pic, .jcToppic .vip01pic {
        width: 58px;
        height: 58px;
        display: block;
        position: absolute;
        top: 0px;
        left: 0px;
        background: url(${path}/scripts/bigpicroll/images/vipico.gif) no-repeat 0 0;
        z-index: 200
    }

    .vip02pic {
        width: 39px;
        height: 39px;
        display: block;
        position: absolute;
        top: 0px;
        left: 0px;
        background: url(${path}/scripts/bigpicroll/images/vipico.gif) no-repeat -80px 0;
        z-index: 200
    }

    .flashlist .f_out .picintro {
        height: 84px;
        background: url(${path}/scripts/bigpicroll/images/hdpng.png) no-repeat scroll 0 -1px;
        position: absolute;
        bottom: 0;
        left: 0;
        width: ${param.width}px;
        z-index: 999;
        padding: 0 20px;
    }

    .picintro h2 a {
        color: #FFF;
        font-family: "微软雅黑";
        font-size: 20px;
        font-weight: bold;
        line-height: 37px;
    }

    .picintro p {
        color: #767676;
        line-height: 20px;
    }

    .picintro p a {
        color: #abadac;
    }

    .flash_tab {
        height: 26px;
        background: url('${path}/scripts/bigpicroll/images/hdbg.gif'${path}/scripts/bigpicroll/images/hdbg.gif') no-repeat scroll 0 0;
        padding-top: 19px;
    }

    .flash_tab .tabs {
        width: 200px;
        height: 8px;
        overflow: hidden;
        margin: auto;
    }

    .flash_tab .tabs ul li {
        width: 33px;
        height: 8px;
        float: left;
        display: block;
    }

    .flash_tab .tabs ul li.opdiv, .noopdiv {
        padding: 0;
        opacity: 1
    }

    .flash_tab .tabs ul li.opdiv a {
        width: 21px;
        height: 8px;
        background: url('${path}/scripts/bigpicroll/images/hdsmall.gif'${path}/scripts/bigpicroll/images/hdsmall.gif') no-repeat scroll -41px -1239px;
        display: block;
    }

    .flash_tab .tabs ul li.opdiv a:hover {
        background-position: -8px -1239px;
    }

    .flash_tab .tabs ul li.noopdiv a {
        width: 21px;
        height: 8px;
        background: url('${path}/scripts/bigpicroll/images/hdsmall.gif'${path}/scripts/bigpicroll/images/hdsmall.gif') no-repeat scroll -8px -1239px;
        display: block;
    }

</style>
<script type="text/javascript">
    var FeatureList = function (fobj, options) {
        function feature_slide(nr) {
            if (typeof nr == "undefined") {
                nr = visible_idx + 1;
                nr = nr >= total_items ? 0 : nr;
            }

            tabs.removeClass(onclass).addClass(offclass).filter(":eq(" + nr + ")").removeClass(offclass).addClass(onclass);
            output.stop(true, true).filter(":visible").hide();
            output.filter(":eq(" + nr + ")").fadeIn("slow", function () {
                visible_idx = nr;
            });
        }

        fobj = (typeof(fobj) == 'string') ? $(fobj) : fobj;
        fobj = $(fobj).eq(0);
        if (!fobj || fobj.size() == 0) return;

        //轮询间隔，默认2S
        var options = options || {};
        var visible_idx = options.startidx || 0;
        var onclass = options.onclass || "current";
        var offclass = options.offclass || "";
        var speed = options.speed || 10000;
        options.pause_on_act = options.pause_on_act || "click";
        options.interval = options.interval || 50000;

        var tabs = fobj.find(".f_tabs .f_tab");
        var output = fobj.find(".f_out");
        var total_items = tabs.length;

        //初始设定
        output.hide().eq(visible_idx).fadeIn("slow");
        tabs.eq(visible_idx).addClass(onclass);

        if (options.interval > 0) {
            var timer = setInterval(function () {
                feature_slide();
            }, options.interval);
            output.mouseenter(function () {
                clearInterval(timer);
            }).mouseleave(function () {
                        clearInterval(timer);
                        timer = setInterval(function () {
                            feature_slide();
                        }, options.interval);
                    });
            if (options.pause_on_act == "mouseover") {
                tabs.mouseenter(function () {
                    clearInterval(timer);

                    var idx = tabs.index($(this));
                    feature_slide(idx);
                }).mouseleave(function () {
                            clearInterval(timer);
                            timer = setInterval(function () {
                                feature_slide();
                            }, options.interval);
                        });
            } else {
                tabs.click(function () {
                    clearInterval(timer);
                    var idx = tabs.index($(this));
                    feature_slide(idx);
                });
            }
        }
    }
    $(function () {
        FeatureList(".f_list", {
            "onclass": "noopdiv",
            "offclass": "opdiv",
            "pause_on_act": "mouseover",
            "interval": 5000,
            "speed": 5
        });
    })
</script>
<!-- 代码 开始 -->
<div class="hdwrap">
<div class="hdflash f_list">
<div class="flashlist">
    <div class="f_out">
        <a href="#" target="_blank">
            <img src="${path}/scripts/bigpicroll/images/01.jpg" alt="意外的恋爱时光 Love Speaks (2013)" title="意外的恋爱时光 Love Speaks (2013)"
                 width="${param.width}" height="${param.height}"/>
        </a>

        <div class="picintro">
            <h2>
                <a href="#" title="意外的恋爱时光 Love Speaks (2013)"
                   target="_blank">
                    意外的恋爱时光 Love Speaks (2013)
                </a>
            </h2>

            <p>
                该片是一部具备亲切自然风格的浪漫气质喜剧，讲述毫无交集的两个人在寻觅爱情，抓住爱情的路上遭遇"意外"，却在意外后经历一段浪漫又离奇，文艺又幽默的"恋爱时光"的爱情奇遇记，探讨两个社会地位、人生阅历大相径庭的都市男女在剥开物质外衣，摒弃金钱元素后，对于爱情真谛和理想婚姻的追寻。...
            </p>
        </div>
    </div>
    <div class="f_out">
        <a href="#" target="_blank">
            <img src="${path}/scripts/bigpicroll/images/02.jpg" alt="钢铁侠3Iron Man 3 (2013)" title="钢铁侠3Iron Man 3 (2013)"
                 width="${param.width}" height="${param.height}"/>
        </a>

        <div class="picintro">
            <h2>
                <a href="#" title="钢铁侠3Iron Man 3 (2013)"
                   target="_blank">
                    钢铁侠3Iron Man 3 (2013)
                </a>
            </h2>

            <p>
                在《钢铁侠3》中，托尼·斯塔克将遭遇到一个能力似乎没有界限的强敌的挑战。这个人毁坏了斯塔克的生活，而斯塔克只有依靠着自己精良的高科技的装备才能去寻找究竟是谁才是幕后的元凶。在寻找的过程中，斯塔克非常依赖自己的钢铁服，每一次转折都在测试着他的勇气。当他最终找到强敌，
                并且准备展开反戈一击的时候，斯塔克顿时发现了自...
            </p>
        </div>
    </div>
    <div class="f_out">
        <a href="#" target="_blank">
            <img src="${path}/scripts/bigpicroll/images/03.jpg" alt="致我们终将逝去的青春 So Young / To Our Youth That To Fading Away (2013)"
                 title="致我们终将逝去的青春 So Young / To Our Youth That To Fading Away (2013)" width="${param.width}"
                 height="${param.height}"/>
        </a>

        <div class="picintro">
            <h2>
                <a href="#" title="致我们终将逝去的青春 So Young / To Our Youth That To Fading Away (2013)"
                   target="_blank">
                    致我们终将逝去的青春 So Young / To Our Youth That To Fading Away (2013)
                </a>
            </h2>

            <p>
                自喻为"玉面小飞龙"的郑微，洋溢着青春活力，心怀着对邻家哥哥——林静浓浓的爱意，来到大学。可是当她联系林静的时候，却发现出国的林静并没有告诉她任何消息。生性豁达的她，埋藏起自己的爱情，享受大学时代的快乐生活。却意外地爱上同学校的陈孝正，板正、自闭而又敏感、自尊的陈孝正却在毕业的时候又选择了出国放弃了郑微。
                几...
            </p>
        </div>
    </div>
    <div class="f_out">
        <a href="#" target="_blank">
            <img src="${path}/scripts/bigpicroll/images/04.jpg" alt=" 疯狂原始人 The Croods (2013)" title=" 疯狂原始人 The Croods (2013)"
                 width="${param.width}" height="${param.height}"/>
        </a>

        <div class="picintro">
            <h2>
                <a href="#" title=" 疯狂原始人 The Croods (2013)"
                   target="_blank">
                    疯狂原始人 The Croods (2013)
                </a>
            </h2>

            <p>
                原始人克鲁德一家六口在老爸Grug（尼古拉斯·凯奇 Nicolas Cage 配音）的庇护下生活。每天抢夺鸵鸟蛋为食，躲避野兽的追击，每晚听老爸叙述同一个故事，在山洞里过着一成不变的生活。大女儿EEP（艾玛·斯通
                Emma Stone 配音）是一个和老爸性格截然相反的充满好奇心的女孩，她不满足一辈子留在这个小山洞里，一心想要追逐山洞外面的世...
            </p>
        </div>
    </div>
    <div class="f_out">
        <a href="#" target="_blank">
            <img src="${path}/scripts/bigpicroll/images/05.jpg" alt="同谋 Conspirators (2013)"
                 title="同谋 Conspirators (2013)" width="${param.width}" height="${param.height}"/>
        </a>

        <div class="picintro">
            <h2>
                <a href="http://www.afeidy.com/?s=vod-read-id-1985.html" title="同谋 Conspirators (2013)"
                   target="_blank">
                    同谋 Conspirators (2013)
                </a>
            </h2>

            <p>
                泰国私家侦探陈探（郭富城
                饰）年幼父母失踪，长大后为了寻找父母的踪迹而当上私家侦探，在查案中发现父母已死，誓要找到杀害父母的凶手。几经辗转终于得知当年父母的朋友柴叔在马来西亚，便只身前往，然而柴叔却说知道真相的鹏哥已经死了。此时陈探接到电话得知家中被盗，连夜赶回家后发现屋子被翻了个底朝天，甚至连自己都不知道的...
            </p>
        </div>
    </div>
    <div class="f_out">
        <a href="#" target="_blank">
            <img src="${path}/scripts/bigpicroll/images/06.jpg" alt="枪花 (2013)" title="枪花 (2013)"
                 width="${param.width}" height="${param.height}"/>
        </a>

        <div class="picintro">
            <h2>
                <a href="#" title="枪花 (2013)"
                   target="_blank">
                    枪花 (2013)
                </a>
            </h2>

            <p>
                由闻名编剧朱睿执笔，刘国豪导演拍照的时髦谍战剧《枪花》一反往日谍战剧严厉、烦闷的个性，大展轻松、诙谐的扮演方法，是声称首部美剧个性的国产时髦谍战剧。与X女奸细相同，本剧的主角工作也死女间谍，在国际军事史上女间谍是不行疏忽的战役精灵，她们是男人战役国际里的美丽奇葩。天使的容颜、魔鬼的身段，时而柔情妩媚，时而机敏...
            </p>
        </div>
    </div>
    <div class="f_out">
        <a href="#" target="_blank">
            <img src="${path}/scripts/bigpicroll/images/07.jpg" alt="盛夏晚晴天 (2013)" title="盛夏晚晴天 (2013)"
                 width="${param.width}" height="${param.height}"/>
        </a>

        <div class="picintro">
            <h2>
                <a href="#" title="盛夏晚晴天 (2013)"
                   target="_blank">
                    盛夏晚晴天 (2013)
                </a>
            </h2>

            <p>
                《盛夏晚晴天》全集在线观看，由欢瑞世纪影视传媒股份有限公司和美亚长城传媒（北京）有限公司联合出品。刘恺威首次担任制片人。故事讲述了夏晚晴（杨幂饰）在遭遇好友算计、男友悔婚并落入连环圈套毫无退路的窘境下，被高智商、深城府的"花花公子"乔津帆（刘恺威饰）解脱困境，然而这一切不过是更加复杂庞大的圈套的初始，夏晚晴的...
            </p>
        </div>
    </div>
    <div class="f_out">
        <a href="#" target="_blank">
            <img src="${path}/scripts/bigpicroll/images/08.jpg" alt="那金花和她的女婿 (2013)"
                 title="那金花和她的女婿 (2013)" width="${param.width}" height="${param.height}"/>
        </a>

        <div class="picintro">
            <h2>
                <a href="#" title="那金花和她的女婿 (2013)"
                   target="_blank">
                    那金花和她的女婿 (2013)
                </a>
            </h2>

            <p>
                那金花是位非常讲究的老太太，有点势利，精于算计，身上散发着典型的小市民习气。她有三个女儿，大女儿玉英是她的心头肉，但是玉英却远嫁北方与当地人何兆海结了婚。那金花一气之下与其断绝了联系。没想到好景不长，因工厂发生事故，玉英以身殉职，留下儿子何威。悲痛欲绝的那金花发誓要将孙子夺来自己身边。在丈母娘那金花的要求之下...
            </p>
        </div>
    </div>
    <div class="f_out">
        <a href="#" target="_blank">
            <img src="${path}/scripts/bigpicroll/images/09.jpg" alt="金枝欲孽2 金枝慾孽貳 (2013)" title="金枝欲孽2 金枝慾孽貳 (2013)"
                 width="${param.width}" height="${param.height}"/>
        </a>

        <div class="picintro">
            <h2>
                <a href="#" title="金枝欲孽2 金枝慾孽貳 (2013)"
                   target="_blank">
                    金枝欲孽2 金枝慾孽貳 (2013)
                </a>
            </h2>

            <p>
                《金枝欲孽贰》讲述另一个时空、另一个后宫、另一番截然不同的"金枝欲孽"风貌。嘉庆年间惇太妃钮祜禄·宛琇（伍咏薇饰）为宣泄对亲姐姐如妃钮祜禄·如玥（邓萃雯饰）的不满，故意在后宫散播种种谣言，充满机心的乳娘布雅穆齐·湘菱（蔡少芬饰）借势为他人查证真伪来谋利生财，亦顺理成章成了如妃的亲信，二人情同姊妹，直至戏班老倌...
            </p>
        </div>
    </div>
</div>
<div class="flash_tab">
    <div class="tabs f_tabs" style="width:330px;">
        <ul>
            <li class="f_tab opdiv">
                <a href="javascript:void(0);" title="意外的恋爱时光 Love Speaks (2013)">
                </a>
            </li>
            <li class="f_tab opdiv">
                <a href="javascript:void(0);" title="钢铁侠3Iron Man 3 (2013)">
                </a>
            </li>
            <li class="f_tab opdiv">
                <a href="javascript:void(0);" title="致我们终将逝去的青春 So Young / To Our Youth That To Fading Away (2013)">
                </a>
            </li>
            <li class="f_tab opdiv">
                <a href="javascript:void(0);" title=" 疯狂原始人 The Croods (2013)">
                </a>
            </li>
            <li class="f_tab opdiv">
                <a href="javascript:void(0);" title="同谋 Conspirators (2013)">
                </a>
            </li>
            <li class="f_tab opdiv">
                <a href="javascript:void(0);" title="枪花 (2013)">
                </a>
            </li>
            <li class="f_tab opdiv">
                <a href="javascript:void(0);" title="盛夏晚晴天 (2013)">
                </a>
            </li>
            <li class="f_tab opdiv">
                <a href="javascript:void(0);" title="那金花和她的女婿 (2013)">
                </a>
            </li>
            <li class="f_tab opdiv">
                <a href="javascript:void(0);" title="金枝欲孽2 金枝慾孽貳 (2013)">
                </a>
            </li>
        </ul>
    </div>
</div>
</div>
</div>
<!-- 代码 结束 -->