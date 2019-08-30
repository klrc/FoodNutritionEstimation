
# %%
import xlrd
import sys
sys.path.append('.')

# %%


def load_table(file_path, sheet=1):
    data = xlrd.open_workbook(file_path)
    return data.sheet_by_name(f'Sheet{sheet}')


# %%
stats = {}
table = load_table('.downloads/stats.xlsx')
for x in range(table.nrows):
    line = table.row_values(x)
    # kcal, g=4kcal, g=4kcal, g=9kcal
    _id = line[0]
    _w, _n0, _n1, _n2, _n3 = [v if type(v) == float else 0 for v in line[1:]]
    stats[_id] = [_n0*_w/100, _n1*4*_w/100, _n2*4*_w/100, _n3*9*_w/100]

print(stats)


# %%


def read_foods(table, start_from=1, fmt=1):
    current_food = None
    current_volume = None
    current_ids = {}
    ret = {}
    for line in range(start_from, table.nrows):
        if fmt == 1:
            _food, _id, _, _w, _volume, _, _, _ = table.row_values(line)
        elif fmt == 2:
            _food, _id, _w, _volume = table.row_values(line)
        if _food != '':
            if current_food is not None:
                ret[_food] = {
                    'ids': current_ids.copy(),
                    'volume': current_volume,
                }
            current_ids = {}
            current_food = _food
            current_volume = 0
        if _volume != '':
            current_volume += _volume
        if _w != '':
            if _id == '':
                current_ids[_food] = _w
            else:
                current_ids[_id] = _w
    return ret


rec_1 = load_table('.downloads/recipe_1.xlsx', sheet=4)
dic_1 = read_foods(rec_1)
rec_2 = load_table('.downloads/recipe_2.xlsx')
dic_2 = read_foods(rec_2, start_from=0, fmt=2)


# 利用xxx，实现xxx
# s1、获取了xxx，并对xxx进行标注，构建xx数据集


dic_1.update(dic_2)
replace_dir = {
    '牛肉块': '牛肉（肥瘦）',
    '牛肉片': '牛肉（瘦）',
    '豌豆（带夹）': '牛油',
    '荠菜': '',
    '粉丝（干）':,
    '内酯豆腐':,
}

for food in dic_1.keys():
    volume = dic_1[food]['volume']

    try:
        nutri = [0, 0, 0, 0]
        for _id in dic_1[food]['ids'].keys():
            _w = dic_1[food]['ids'][_id]
            # if _id in replace_dir.keys():
            #     _id = replace_dir[_id]
            _nutri = [_w*x for x in stats[_id]]
            # print(_id, _nutri)
            nutri = [a+b for a, b in zip(nutri, _nutri)]

        # print("×", food, nutri, volume)
    except KeyError as e:
        print(e)

# %%
