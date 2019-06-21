import re
import requests
from urllib import error
from bs4 import BeautifulSoup
import os

num = 0
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


def dowmloadPicture(html, keyword, _dir):
    global num
    # t =0
    pic_url = re.findall('"objURL":"(.*?)",', html, re.S)  # 先利用正则表达式找到图片url
    print('找到关键词:' + keyword + '的图片，即将开始下载图片...')
    for each in pic_url:
        print('正在下载第' + str(num + 1) + '张图片，图片地址:' + str(each))
        try:
            if each is not None:
                pic = requests.get(each, timeout=7)
            else:
                continue
        except BaseException:
            print('错误，当前图片无法下载')
            continue
        else:
            string = f'{_dir}/{keyword}_{num}.jpg'
            fp = open(string, 'wb')
            fp.write(pic.content)
            fp.close()
            num += 1


def launch_single_word(word, path, num_pages):
    base = 'http://image.baidu.com/search/flip?tn=baiduimage&ie=utf-8&word=' + word
    tot = Find(base)
    # _rec = recommend(base)  # 记录相关推荐
    print('经过检测%s类图片共有%d张' % (word, tot))
    if not os.path.exists(path):
        os.mkdir(path)
    for i in range(num_pages):
        try:
            url = f'{base}&pn={i}'
            print(url)
            result = requests.get(url, timeout=10)
        except error.HTTPError as e:
            print(e)
        else:
            dowmloadPicture(result.text, word, path)


class Config():
    save_path = '.downloads/unlabeled_baidu_food_imgs'
    words = ['土豆炖牛肉', '血魔', 'NEC']
    num_pages = 1


def launch(config):
    path = config.save_path
    num_pages = config.num_pages
    for word in config.words:
        launch_single_word(word, path, num_pages)


if __name__ == '__main__':  # 主函数入口
    import sys
    sys.path.append('.')
    launch(Config())
