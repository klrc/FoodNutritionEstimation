import re
import requests
from urllib import error
from bs4 import BeautifulSoup
import os

List = []


def Find(url):
    global List
    print('正在检测图片总数，请稍等.....')
    t = 0
    s = 0
    while t < 1000:
        Url = url + str(t)
        try:
            Result = requests.get(Url, timeout=7)
        except BaseException:
            t = t + 60
            continue
        else:
            result = Result.text
            pic_url = re.findall('"objURL":"(.*?)",',
                                 result, re.S)  # 先利用正则表达式找到图片url
            s += len(pic_url)
            if len(pic_url) == 0:
                break
            else:
                List.append(pic_url)
                t = t + 60
    return s


def recommend(url):
    Re = []
    try:
        html = requests.get(url)
    except error.HTTPError as e:
        print(e)
        return
    else:
        html.encoding = 'utf-8'
        bsObj = BeautifulSoup(html.text, 'html.parser')
        div = bsObj.find('div', id='topRS')
        if div is not None:
            listA = div.findAll('a')
            for i in listA:
                if i is not None:
                    Re.append(i.get_text())
        return Re


def dowmloadPicture(html, keyword, _dir, stored_imgs, max_imgs):
    pic_url = re.findall('"objURL":"(.*?)",', html, re.S)  # 先利用正则表达式找到图片url
    print(f'Downloading page from keyword: {keyword}')
    for each in pic_url:
        stored_imgs += 1
        if max_imgs < stored_imgs:
            break
        print(f'Downloading {stored_imgs} of {max_imgs}: {each}')
        try:
            if each is not None:
                pic = requests.get(each, timeout=7)
            else:
                continue
        except BaseException:
            print('错误，当前图片无法下载')
            continue
        else:
            string = f'{_dir}/{keyword}_{stored_imgs}.jpg'
            fp = open(string, 'wb')
            fp.write(pic.content)
            fp.close()
    return stored_imgs


def launch_single_word(word, path, num_pages, num_imgs):
    base = 'http://image.baidu.com/search/flip?tn=baiduimage&ie=utf-8&word=' + word
    tot = Find(base)
    # _rec = recommend(base)  # 记录相关推荐
    print('经过检测%s类图片共有%d张' % (word, tot))
    if not os.path.exists(path):
        os.mkdir(path)
    stored_imgs = 0
    for i in range(num_pages):
        try:
            url = f'{base}&pn={i}'
            print(url)
            result = requests.get(url, timeout=10)
        except error.HTTPError as e:
            print(e)
        else:
            stored_imgs += dowmloadPicture(result.text,
                                           word, path, stored_imgs, num_imgs)
            if stored_imgs >= num_imgs:
                break


class Config():
    save_path = '.downloads/unlabeled_baidu_food_imgs_3'
    words = ['白面条']
    # words = ['扁豆炒肉丝', '毛豆炒丝瓜', '毛豆炒肉丝', '清炒河虾', '清蒸鲈鱼', '炒豆角', '炖土鸡', '烤红薯', '烧麦', '猪肉白菜水饺', '白灼河虾', '红烧土豆炖排骨', '红烧大排', '红烧带鱼', '红烧排骨', '红烧鲫鱼', '红烧鸡块', '红烧鸡腿', '红烧鸭块', '绿豆杂粮饭', '菜肉馄饨', '葱油梭子蟹', '面包', '白面条', '水煮玉米', '炒年糕', '蒸南瓜', '炒南瓜', '糖醋藕丝',
    #          '山药木耳肉片', '肉末炒粉丝', '荷包蛋', '荠菜豆腐羹', '家常豆腐', '油焖茭白', '小排汤', '西红柿炒鸡蛋', '红烧猪蹄', '红烧带鱼', '清蒸小黄鱼', '咸菜小黄鱼汤', '生三文鱼', '香煎三文鱼', '葱油花蛤', '蒸芋艿', '葱油芋艿', '水煮荸荠', '炒蚕豆', '水煮豌豆', '豌豆玉米虾仁']
    # words = ['糖醋排骨', '清炒土豆丝', '土豆炖牛肉', '咖喱牛肉', '包子', '酱牛肉']
    num_pages = 5
    target_num_imgs = 80


def launch(config):
    path = config.save_path
    num_pages = config.num_pages
    num_imgs = config.target_num_imgs
    for word in config.words:
        launch_single_word(word, path, num_pages, num_imgs)


if __name__ == '__main__':  # 主函数入口
    import sys
    sys.path.append('.')
    launch(Config())
