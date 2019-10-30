package com.example.camera;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

public class FoodHtml extends AppCompatActivity {
    private String body = "\"\n" +
            "\n" +
            "<!DOCTYPE html>\n" +
            "\n" +
            "<!--[if lt IE 7]> <html class=\"lt-ie9 lt-ie8 lt-ie7 show-anti-ie\"> <![endif]-->\n" +
            "<!--[if IE 7]>    <html class=\"lt-ie9 lt-ie8 show-anti-ie\"> <![endif]-->\n" +
            "<!--[if IE 8]>    <html xmlns:ng=\"http://angularjs.org\" class=\"lt-ie9\"> <![endif]-->\n" +
            "<!--[if gt IE 8]><!--> <html class=\"\"> <!--<![endif]-->\n" +
            "\n" +
            "<head>\n" +
            "  \n" +
            "\n" +
            "\n" +
            "    <title>\n" +
            "【啤酒黄焖鸡的做法步骤图，啤酒黄焖鸡怎么做好吃】我煮饭你洗碗_下厨房</title>\n" +
            "\n" +
            "    <meta charset=\"utf-8\">\n" +
            "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\">\n" +
            "    <meta http-equiv=\"content-type\" content=\"text/html;charset=UTF-8;\">\n" +
            "    <meta http-equiv=\"x-dns-prefetch-control\" content=\"on\">\n" +
            "    <link rel=\"dns-prefetch\" href=\"//s.chuimg.com\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width\">\n" +
            "    <meta name=\"apple-itunes-app\" content=\"app-id=460979760\"/>\n" +
            "    <meta name=\"baidu-site-verification\" content=\"HNAbHIxmbo\" />\n" +
            "    <meta name=\"renderer\" content=\"webkit\">\n" +
            "    <link href=\"https://s.chuimg.com/favicon.ico\" rel=\"shortcut icon\">\n" +
            "\n" +
            "    <script src=\"//dup.baidustatic.com/js/ds.js\"></script>\n" +
            "  \n" +
            "\n" +
            "<link rel=\"canonical\" href=\"https://www.xiachufang.com/recipe/104064260/\">\n" +
            "<link rel=\"alternate\" media=\"only screen and (max-width: 640px)\" href=\"https://m.xiachufang.com/recipe/104064260/\">\n" +
            "<link rel=\"alternate\" media=\"handheld\" href=\"https://m.xiachufang.com/recipe/104064260/\">\n" +
            "\n" +
            "\n" +
            "<meta name=\"keywords\" content=\"啤酒黄焖鸡,啤酒黄焖鸡的做法,啤酒黄焖鸡的家常做法,啤酒黄焖鸡的详细做法,怎么做啤酒黄焖鸡,啤酒黄焖鸡怎么做,啤酒黄焖鸡的最正宗做法,家常菜,快手菜,宴客菜\">\n" +
            "\n" +
            "<meta name=\"description\" content=\"【啤酒黄焖鸡】1.配料准备：香菇切片，青椒红辣椒切段备用，雪花啤酒一瓶备用；2.调料准备：大蒜子、姜切片，香叶、干辣椒、八角、桂皮洗净，起锅烧红放油炸香备用；3.鸡去毛去内脏后剁成小块，洗净沥干水，加料酒、生抽、胡椒粉、盐适量拌匀腌制20分钟，待鸡肉入味后沥干水；4.起锅放油烧至七成热，撒几料花椒炝一下，闻到花椒味捞出花椒，再将鸡倒入翻炒；5.将步骤2已炸香的调料倒入锅内；6.先大火翻炒到鸡肉变色再改小火翻炒至...\">\n" +
            "\n" +
            "<meta property=\"og:title\" content=\"啤酒黄焖鸡\">\n" +
            "<meta property=\"og:url\" content=\"https://www.xiachufang.com/recipe/104064260/\">\n" +
            "<meta property=\"og:type\" content=\"website\">\n" +
            "<meta property=\"og:description\" content=\"1.配料准备：香菇切片，青椒红辣椒切段备用，雪花啤酒一瓶备用；2.调料准备：大蒜子、姜切片，香叶、干辣椒、八角、桂皮洗净，起锅烧红放油炸香备用；3.鸡去毛去内脏后剁成小块，洗净沥干水，加料酒、生抽、胡椒粉、盐适量拌匀腌制20分钟，待鸡肉入味后沥干水；4.起锅放油烧至七成热，撒几料花椒炝一下，闻到花椒味捞出花椒，再将鸡倒入翻炒；5.将步骤2已炸香的调料倒入锅内；6.先大火翻炒到鸡肉变色再改小火翻炒至...\">\n" +
            "<meta property=\"og:image\" content=\"http://i2.chuimg.com/e907a4b0816a4c208a26e1efe9aa1c68_872w_1193h.jpg?imageView2/1/w/280/h/216/interlace/1/q/90\">\n" +
            "\n" +
            "<script type=\"text/javascript\">\n" +
            "_gaq=[['_setAccount', 'UA-22069234-1'],\n" +
            "      ['_setCustomVar', 1, 'user', 'anonymous', 3],\n" +
            "      ['_setCustomVar', 4, 'dish_title', 'dish_title_2', 3],\n" +
            "      ['_setCustomVar', 5, 'recommend_recipe_list', 'recommend_recipe_list_0', 3],\n" +
            "      ['_trackPageview'],['_trackPageLoadTime']];\n" +
            "\n" +
            "\n" +
            "(function(){\n" +
            "    var ga = document.createElement('script');\n" +
            "    ga.type = 'text/javascript';\n" +
            "    ga.async = true;\n" +
            "    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';\n" +
            "    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);\n" +
            "})()\n" +
            "</script>\n" +
            "\n" +
            "</head>\n" +
            "\n" +
            "\n" +
            "\n" +
            "<body>\n" +
            "\n" +
            "<!--[if IE]>\n" +
            "<div class=\"anti-ie hidden\">\n" +
            "  <div class=\"wrapper\">\n" +
            "    <div class=\"collapsed\">\n" +
            "    啊！页面乱了！因为下厨房不再支持IE8以下版本，我们推荐你<a target=\"_blank\" href=\"https://ie.microsoft.com\" rel=\"nofollow\">更新你的浏览器</a>，或使用<a href=\"https://www.baidu.com/s?wd=%E6%B5%8F%E8%A7%88%E5%99%A8\" rel=\"nofollow\" target=\"_blank\">其他浏览器</a>。\n" +
            "    <a href=\"javascript:void(0)\" class=\"button open\">为什么</a>\n" +
            "    </div>\n" +
            "\n" +
            "    <div class=\"expanded pure-g hidden\">\n" +
            "      <div class=\"pure-u-1-4\">\n" +
            "        <div class=\"title bold-font\">\n" +
            "        IE的故事\n" +
            "        </div>\n" +
            "      </div>\n" +
            "      <div class=\"pure-u-1-2\">\n" +
            "        <div class=\"story \">\n" +
            "          <img src=\"https://s.chuimg.com/pic/2013/ie-story.png\" alt=\"IE的故事\">\n" +
            "        </div>\n" +
            "      </div>\n" +
            "      <div class=\"pure-u-1-4 align-right\">\n" +
            "        <a href=\"javascript:void(0)\" class=\"normal-font gray-link close\">关闭</a>\n" +
            "      </div>\n" +
            "    </div>\n" +
            "  </div>\n" +
            "</div>\n" +
            "<![endif]-->\n" +
            "\n" +
            "<div class=\"popup-layer\">\n" +
            "\n" +
            "\n" +
            "  \n" +
            "  <div class=\"reveal-modal-bg\"></div>\n" +
            "  \n" +
            "<div class=\"reveal-modal large normal-font\" id=\"feedbackModal\">\n" +
            "  \n" +
            "  <div class=\"reveal-modal-title-bar\">\n" +
            "    <div class=\"reveal-modal-title\">意见反馈</div>\n" +
            "    <a href=\"javascript:void(0)\" class=\"close-reveal-modal\" rel=\"nofollow\"></a>\n" +
            "  </div>\n" +
            "  <div class=\"reveal-modal-main\">\n" +
            "    \n" +
            "    \n" +
            "    <form action=\"/feedback/create/\" method=\"POST\">\n" +
            "      <textarea name=\"feedback\" rows=\"3\"></textarea>\n" +
            "      <input type=\"hidden\" name=\"xf\" value=\"None\">\n" +
            "      <input type=\"submit\" value=\"提交\" class=\"button\">\n" +
            "    </form>\n" +
            "  \n" +
            "  \n" +
            "  </div>\n" +
            "\n" +
            "</div>\n" +
            "\n" +
            "\n" +
            "  \n" +
            "\n" +
            "\n" +
            "\n" +


            "<script>\n" +
            "(function() {\n" +
            "    var s = \"_\" + Math.random().toString(36).slice(2);\n" +
            "    document.write('<div id=\"' + s + '\"></div>');\n" +
            "    (window.slotbydup=window.slotbydup || []).push({\n" +
            "        id: '3543483',\n" +
            "        container: s,\n" +
            "        size: '1000,90',\n" +
            "        display: 'inlay-fix',\n" +
            "        async: true\n" +
            "    });\n" +
            "})();\n" +
            "</script>\n" +
            "\n" +
            "</div>\n" +
            "\n" +
            "\n" +
            "<div class=\"page-outer\">\n" +
            "  <!--begin of page-container-->\n" +
            "  <div class=\"page-container\">\n" +
            "    \n" +
            "    \n" +
            "      \n" +
            "\n" +
            "<ol class=\"breadcrumb plain pl10\" itemscope itemtype=\"http://schema.org/BreadcrumbList\">\n" +
            "    <li itemprop=\"itemListElement\" itemscope itemtype=\"http://schema.org/ListItem\" class=\"\">\n" +
            "        <a href=\"/\" title=\"首页\">首页</a>\n" +
            "        <meta itemprop=\"position\" content=\"1\" >\n" +
            "    </li>\n" +
            "    <li itemprop=\"itemListElement\" itemscope itemtype=\"http://schema.org/ListItem\" class=\"\">\n" +
            "        <a href=\"/category/40076/\" title=\"家常菜\">家常菜</a>\n" +
            "        <meta itemprop=\"position\" content=\"2\" >\n" +
            "    </li>\n" +
            "    <li itemprop=\"itemListElement\" itemscope itemtype=\"http://schema.org/ListItem\" class=\"active\">\n" +
            "        <span>啤酒黄焖鸡</span>\n" +
            "        <meta itemprop=\"position\" content=\"3\" >\n" +
            "    </li>\n" +
            "</ol>\n" +
            "\n" +
            "      <div class=\"pure-g\">\n" +
            "        <!-- begin of main-panel -->\n" +
            "        <div class=\"pure-u-2-3 main-panel\">\n" +
            "          \n" +
            "\n" +
            "  <!-- begin of recipe -->\n" +
            "  <div itemscope itemtype=\"http://schema.org/Recipe\">\n" +
            "    <h1 class=\"page-title\" itemprop=\"name\">\n" +
            "      啤酒黄焖鸡\n" +
            "    </h1>\n" +
            "    <div class=\"block recipe-show\">\n" +
            "\n" +
            "      <div class=\"cover image expandable block-negative-margin\"\n" +
            "        style=\"background-image: url(http://i2.chuimg.com/e907a4b0816a4c208a26e1efe9aa1c68_872w_1193h.jpg?imageView2/2/w/660/interlace/1/q/90)\">\n" +
            "        <img src=\"http://i2.chuimg.com/e907a4b0816a4c208a26e1efe9aa1c68_872w_1193h.jpg?imageView2/2/w/660/interlace/1/q/90\" alt=\"啤酒黄焖鸡的做法\" width=\"660\" itemprop=\"image\">\n" +
            "      </div>\n" +
            "\n" +
            "\n" +
            "      <div class=\"container pos-r pb20 has-bottom-border\">\n" +
            "        <div class=\"stats clearfix\">\n" +
            "          <div class=\"score float-left\" itemprop=\"aggregateRating\" itemscope itemtype=\"http://schema.org/aggregateRating\">\n" +
            "            <span class=\"number\" itemprop=\"ratingValue\">7.3</span>\n" +
            "            <meta itemprop=\"bestRating\" content=\"10\">\n" +
            "            <meta itemprop=\"ratingCount\" content=\"19\">\n" +
            "            <span class=\"title\">综合评分</span>\n" +
            "          </div>\n" +
            "\n" +
            "          <div class=\"cooked float-left\">\n" +
            "            <span class=\"number\">24</span>\n" +
            "            <span class=\"title\">人做过这道菜</span>\n" +
            "          </div>\n" +
            "        </div>\n" +
            "        <div class=\"collect pure-g align-right\">\n" +
            "            <a href=\"/auth/login/?path=/recipe/104064260/\" class=\"button large-button collect-button\" rel=\"nofollow\">收藏</a>\n" +
            "        </div>\n" +
            "      </div>\n" +
            "\n" +
            "      \n" +
            "      <div class=\"rate-dialog block-negative-margin\">\n" +
            "      </div>\n" +
            "\n" +
            "      \n" +
            "\n" +
            "\n" +
            "      <div class=\"author\" itemprop=\"author\" itemscope itemtype=\"http://schema.org/Person\">\n" +
            "          <a href=\"/cook/129046688/\" title=\"我煮饭你洗碗的厨房\" class=\"avatar-link avatar\">\n" +
            "            <img src=\"http://i1.chuimg.com/a81bbd4d72b84fe1b7f1cd45ca49434f_1080w_1080h.jpg@2o_50sh_1pr_1l_60w_60h_1c_1e_90q_1wh\" alt=\"我煮饭你洗碗的厨房\" width=\"60\" height=\"60\">\n" +
            "            &nbsp;<span itemprop=\"name\">我煮饭你洗碗</span>\n" +
            "          </a>\n" +
            "      </div>\n" +
            "\n" +
            "      \n" +
            "\n" +
            "      <div class=\"desc mt30\" itemprop=\"description\">\n" +
            "        亲戚送来一鸡，问夫如何食之？夫道黄焖鸡也，我又问夫来个啤酒黄焖鸡如何？夫言只食过黄焖鸡与啤酒鸭，未曾食过啤酒黄焖鸡，今托贤妻之富尝二者精华，且不快哉！<br>小火细煨，二椒发之，鸡肉、香菇各奉其鲜，一口即醉 ，啤酒焖鸡肉，质鲜嫩、汤汁浓郁、口口啤酒香。一份黄焖鸡锅中汤还冒着泡，袅袅热气浓郁香味真诱人口馋！<br>现在的生活水平高越来越讲究口味与美食，现在食物吃的方法确实多，加之人们都在不断学习创新，经过不同的人不同的做法不同的口味，做出不同的美食。对吃而言，喜欢就好，好吃就是美食，感谢铁友们的关注，欢迎多多留言评论交流，厨艺共同进步！\n" +
            "      </div>\n" +
            "\n" +
            "      \n" +
            "        \n" +
            "        <h2>\n" +
            "          用料 &nbsp;\n" +
            "        </h2>\n" +
            "        <div class=\"ings\">\n" +
            "          <table>\n" +
            "              \n" +
            "              <tr itemprop=\"recipeIngredient\">\n" +
            "                <td class=\"name\">\n" +
            "                  \n" +
            "\n" +
            "    土鸡\n" +
            "\n" +
            "                </td>\n" +
            "                <td class=\"unit\">\n" +
            "                    一只\n" +
            "                </td>\n" +
            "              </tr>\n" +
            "              \n" +
            "              <tr itemprop=\"recipeIngredient\">\n" +
            "                <td class=\"name\">\n" +
            "                  \n" +
            "\n" +
            "    雪花啤酒\n" +
            "\n" +
            "                </td>\n" +
            "                <td class=\"unit\">\n" +
            "                    一瓶/500ml\n" +
            "                </td>\n" +
            "              </tr>\n" +
            "              \n" +
            "              <tr itemprop=\"recipeIngredient\">\n" +
            "                <td class=\"name\">\n" +
            "                  \n" +
            "\n" +
            "    青椒\n" +
            "\n" +
            "                </td>\n" +
            "                <td class=\"unit\">\n" +
            "                    一个\n" +
            "                </td>\n" +
            "              </tr>\n" +
            "              \n" +
            "              <tr itemprop=\"recipeIngredient\">\n" +
            "                <td class=\"name\">\n" +
            "                  \n" +
            "\n" +
            "    红辣椒\n" +
            "\n" +
            "                </td>\n" +
            "                <td class=\"unit\">\n" +
            "                    一个\n" +
            "                </td>\n" +
            "              </tr>\n" +
            "              \n" +
            "              <tr itemprop=\"recipeIngredient\">\n" +
            "                <td class=\"name\">\n" +
            "                  \n" +
            "\n" +
            "    香菇\n" +
            "\n" +
            "                </td>\n" +
            "                <td class=\"unit\">\n" +
            "                    三朵\n" +
            "                </td>\n" +
            "              </tr>\n" +
            "              \n" +
            "              <tr itemprop=\"recipeIngredient\">\n" +
            "                <td class=\"name\">\n" +
            "                  \n" +
            "\n" +
            "    姜\n" +
            "\n" +
            "                </td>\n" +
            "                <td class=\"unit\">\n" +
            "                    一块切片\n" +
            "                </td>\n" +
            "              </tr>\n" +
            "              \n" +
            "              <tr itemprop=\"recipeIngredient\">\n" +
            "                <td class=\"name\">\n" +
            "                  \n" +
            "\n" +
            "    干辣椒\n" +
            "\n" +
            "                </td>\n" +
            "                <td class=\"unit\">\n" +
            "                    五个\n" +
            "                </td>\n" +
            "              </tr>\n" +
            "              \n" +
            "              <tr itemprop=\"recipeIngredient\">\n" +
            "                <td class=\"name\">\n" +
            "                  \n" +
            "\n" +
            "    八角\n" +
            "\n" +
            "                </td>\n" +
            "                <td class=\"unit\">\n" +
            "                    三个\n" +
            "                </td>\n" +
            "              </tr>\n" +
            "              \n" +
            "              <tr itemprop=\"recipeIngredient\">\n" +
            "                <td class=\"name\">\n" +
            "                  \n" +
            "\n" +
            "    桂皮\n" +
            "\n" +
            "                </td>\n" +
            "                <td class=\"unit\">\n" +
            "                    三小块\n" +
            "                </td>\n" +
            "              </tr>\n" +
            "              \n" +
            "              <tr itemprop=\"recipeIngredient\">\n" +
            "                <td class=\"name\">\n" +
            "                  \n" +
            "\n" +
            "    香叶\n" +
            "\n" +
            "                </td>\n" +
            "                <td class=\"unit\">\n" +
            "                    三片\n" +
            "                </td>\n" +
            "              </tr>\n" +
            "              \n" +
            "              <tr itemprop=\"recipeIngredient\">\n" +
            "                <td class=\"name\">\n" +
            "                  \n" +
            "\n" +
            "    大蒜子\n" +
            "\n" +
            "                </td>\n" +
            "                <td class=\"unit\">\n" +
            "                    三瓣\n" +
            "                </td>\n" +
            "              </tr>\n" +
            "              \n" +
            "              <tr itemprop=\"recipeIngredient\">\n" +
            "                <td class=\"name\">\n" +
            "                  \n" +
            "\n" +
            "    料酒\n" +
            "\n" +
            "                </td>\n" +
            "                <td class=\"unit\">\n" +
            "                    一汤勺\n" +
            "                </td>\n" +
            "              </tr>\n" +
            "              \n" +
            "              <tr itemprop=\"recipeIngredient\">\n" +
            "                <td class=\"name\">\n" +
            "                  \n" +
            "\n" +
            "    生抽\n" +
            "\n" +
            "                </td>\n" +
            "                <td class=\"unit\">\n" +
            "                    一汤勺\n" +
            "                </td>\n" +
            "              </tr>\n" +
            "              \n" +
            "              <tr itemprop=\"recipeIngredient\">\n" +
            "                <td class=\"name\">\n" +
            "                  \n" +
            "\n" +
            "    盐\n" +
            "\n" +
            "                </td>\n" +
            "                <td class=\"unit\">\n" +
            "                    适量\n" +
            "                </td>\n" +
            "              </tr>\n" +
            "              \n" +
            "              <tr itemprop=\"recipeIngredient\">\n" +
            "                <td class=\"name\">\n" +
            "                  \n" +
            "\n" +
            "    花椒\n" +
            "\n" +
            "                </td>\n" +
            "                <td class=\"unit\">\n" +
            "                    10粒\n" +
            "                </td>\n" +
            "              </tr>\n" +
            "              \n" +
            "              <tr itemprop=\"recipeIngredient\">\n" +
            "                <td class=\"name\">\n" +
            "                  \n" +
            "\n" +
            "    白胡椒粉\n" +
            "\n" +
            "                </td>\n" +
            "                <td class=\"unit\">\n" +
            "                    适量\n" +
            "                </td>\n" +
            "              </tr>\n" +
            "          </table>\n" +
            "        </div>\n" +
            "\n" +
            "      \n" +
            "\n" +
            "        \n" +
            "        <h2 id=\"steps\">\n" +
            "          啤酒黄焖鸡的做法 &nbsp;\n" +
            "        </h2>\n" +
            "\n" +
            "        <div class=\"steps\">\n" +
            "          <ol>\n" +
            "            \n" +
            "            <li class=\"container\" itemprop=\"recipeInstructions\">\n" +
            "              <p class=\"text\" style=\"\">配料准备：香菇切片，青椒红辣椒切段备用，雪花啤酒一瓶备用。</p>\n" +
            "              <img src=\"http://i1.chuimg.com/99f5f40bc05946a68cfbcd189515f0c9_1152w_648h.jpg@2o_50sh_1pr_1l_300w_90q_1wh\" alt=\"啤酒黄焖鸡的做法 步骤1\" width=\"300\">\n" +
            "            </li>\n" +
            "            \n" +
            "            <li class=\"container\" itemprop=\"recipeInstructions\">\n" +
            "              <p class=\"text\" style=\"\">调料准备：大蒜子、姜切片，香叶、干辣椒、八角、桂皮洗净，起锅烧红放油炸香备用。</p>\n" +
            "              <img src=\"http://i2.chuimg.com/3f3a525828e545d4aa7ac759886021a5_648w_1152h.jpg?imageView2/2/w/300/interlace/1/q/90\" alt=\"啤酒黄焖鸡的做法 步骤2\" width=\"300\">\n" +
            "            </li>\n" +
            "            \n" +
            "            <li class=\"container\" itemprop=\"recipeInstructions\">\n" +
            "              <p class=\"text\" style=\"\">鸡去毛去内脏后剁成小块，洗净沥干水，加料酒、生抽、胡椒粉、盐适量拌匀腌制20分钟，待鸡肉入味后沥干水。</p>\n" +
            "              <img src=\"http://i2.chuimg.com/8970e66328bb4d6a87cb84ccd0917106_864w_1152h.jpg?imageView2/2/w/300/interlace/1/q/90\" alt=\"啤酒黄焖鸡的做法 步骤3\" width=\"300\">\n" +
            "            </li>\n" +
            "            \n" +
            "            <li class=\"container\" itemprop=\"recipeInstructions\">\n" +
            "              <p class=\"text\" style=\"\">起锅放油烧至七成热，撒几料花椒炝一下，闻到花椒味捞出花椒，再将鸡倒入翻炒。</p>\n" +
            "              <img src=\"http://i1.chuimg.com/5e8332527d0f49aa86d183b0b33c523d_1152w_648h.jpg@2o_50sh_1pr_1l_300w_90q_1wh\" alt=\"啤酒黄焖鸡的做法 步骤4\" width=\"300\">\n" +
            "            </li>\n" +
            "            \n" +
            "            <li class=\"container\" itemprop=\"recipeInstructions\">\n" +
            "              <p class=\"text\" style=\"\">将步骤2已炸香的调料倒入锅内。</p>\n" +
            "              <img src=\"http://i2.chuimg.com/705b593581ef49a7a34824d0e02c2afe_960w_1706h.jpg?imageView2/2/w/300/interlace/1/q/90\" alt=\"啤酒黄焖鸡的做法 步骤5\" width=\"300\">\n" +
            "            </li>\n" +
            "            \n" +
            "            <li class=\"container\" itemprop=\"recipeInstructions\">\n" +
            "              <p class=\"text\" style=\"\">先大火翻炒到鸡肉变色再改小火翻炒至鸡皮出油。注意不要炒糊了，炒糊了口味就变糊焦味的。</p>\n" +
            "              <img src=\"http://i2.chuimg.com/e99a1e653cd443128aef391fa3678a46_648w_1152h.jpg?imageView2/2/w/300/interlace/1/q/90\" alt=\"啤酒黄焖鸡的做法 步骤6\" width=\"300\">\n" +
            "            </li>\n" +
            "            \n" +
            "            <li class=\"container\" itemprop=\"recipeInstructions\">\n" +
            "              <p class=\"text\" style=\"\">然后倒入啤酒、盐盖上锅盖，大火烧沸后转小火，中间偶尔用铲子翻动一下，以免沾锅烧糊，然后焖烧至鸡肉汤汁收干。</p>\n" +
            "              <img src=\"http://i2.chuimg.com/7a815dc20a724e418fcb2f83093c1c43_1152w_648h.jpg?imageView2/2/w/300/interlace/1/q/90\" alt=\"啤酒黄焖鸡的做法 步骤7\" width=\"300\">\n" +
            "            </li>\n" +
            "            \n" +
            "        </ul>\n" +
            "      </div>\n" +
            "    </div>\n" +
            "  </div>\n" +
            "  <!-- end of pop subjects -->\n" +

            "</html>\n\"";
    private Button back_to_nutri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_html);
        back_to_nutri = findViewById(R.id.backto_nutriinfo);
        back_to_nutri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        WebView webView=(WebView)findViewById(R.id.food_html);
        WebSettings webSettings = webView.getSettings();
        webSettings.setDefaultTextEncodingName("UTF-8");
        webView.loadData(body, "text/html; charset=UTF-8", null);
    }
}
