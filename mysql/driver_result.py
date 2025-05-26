def driver_result(originfilename, persons, resultfilename, fontsize, fontcolor):
    from PIL import Image, ImageDraw, ImageFont

    image_origin = Image.open(originfilename)
    draw = ImageDraw.Draw(image_origin)
    setFont = ImageFont.truetype('/Users/qsx/Desktop/DMS/mysql/utils/simfang.ttf', fontsize)
    for person in persons:
        warning = ''
        result = ''
        attributes = person['attributes']
        # 1双手离开方向盘
        score = attributes['both_hands_leaving_wheel']['score']
        threshold = attributes['both_hands_leaving_wheel']['threshold']
        if score > threshold:
            warning = warning + '双手离开方向盘 '
        result = result + ('双手离开方向盘： {:.5f} \n'.format(score))

        # 2闭眼
        score = attributes['eyes_closed']['score']
        threshold = attributes['eyes_closed']['threshold']
        if score > threshold:
            warning = warning + '闭眼 '
        result = result + ('闭眼： {:.5f} \n'.format(score))

        # 不戴口罩
        score = attributes['no_face_mask']['score']
        threshold = attributes['no_face_mask']['threshold']
        if score > threshold:
            warning = warning + '不戴口罩 '
        result = result + ('不戴口罩： {:.5f} \n'.format(score))

        # 未系安全带
        score = attributes['not_buckling_up']['score']
        threshold = attributes['not_buckling_up']['threshold']
        if score > threshold:
            warning = warning + '未系安全带 '
        result = result + ('未系安全带： {:.5f} \n'.format(score))

        # 抽烟
        score = attributes['smoke']['score']
        threshold = attributes['smoke']['threshold']
        if score > threshold:
            warning = warning + '抽烟 '
        result = result + ('抽烟： {:.5f} \n'.format(score))

        # 不看前方
        score = attributes['not_facing_front']['score']
        threshold = attributes['not_facing_front']['threshold']
        if score > threshold:
            warning = warning + '不看前方 '
        result = result + ('不看前方： {:.5f} \n'.format(score))

        # 使用手机
        score = attributes['cellphone']['score']
        threshold = attributes['cellphone']['threshold']
        if score > threshold:
            warning = warning + '使用手机 '
        result = result + ('使用手机： {:.5f} \n'.format(score))

        # 打哈欠
        score = attributes['yawning']['score']
        threshold = attributes['yawning']['threshold']
        if score > threshold:
            warning = warning + '打哈欠 '
        result = result + ('打哈欠： {:.5f} \n'.format(score))
        # 低头
        score = attributes['head_lowered']['score']
        threshold = attributes['head_lowered']['threshold']
        if score > threshold:
            warning = warning + '低头 '
        result = result + ('低头： {:.5f} \n'.format(score))

        gesture = person['location']
        draw.rectangle(
            (gesture['left'], gesture['top'], gesture['left'] + gesture['width'], gesture['top'] + gesture['height']),
            outline="red")

        if warning == '':
            warning = '无'
        result = result + '需要警告内容：' + warning
        print(result)
        draw.text((gesture['left'], gesture['top']), result, font=setFont, fill=fontcolor)

    image_origin.save(resultfilename, "JPEG")
