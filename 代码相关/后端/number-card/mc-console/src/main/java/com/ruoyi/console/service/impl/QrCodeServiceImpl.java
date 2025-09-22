package com.ruoyi.console.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.entity.ToolConfig;
import com.ruoyi.common.utils.JsonParsingUtils;
import com.ruoyi.common.utils.QiniuUtils;
import com.ruoyi.console.service.QrCodeService;
import com.ruoyi.console.service.ToolConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * 二维码生成相关工具类
 *
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/19 17:13
 */
@Service
@Slf4j
public class QrCodeServiceImpl implements QrCodeService {

    private static final int QUIET_ZONE_SIZE = 4;

    @Resource
    private QiniuUtils qiniuUtils;

    /**
     * 采集图片map
     */
    public Map<String, Object> pictureMap;

    @Resource
    ToolConfigService toolConfigService;

    /***
     * 生成带二维码的 海报图片
     */
    public String getQrCodePosterUrl(String content) {
        try {
            Map<String, Object> map = getPageImg("shopPoster");
            //todo 获取底图
            byte[] bytes = Base64.getDecoder().decode(map.get("poster_img")+"");
            //构建字节数组输入流
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            Image srcImg = ImageIO.read(bais);
            //获取图片的宽
            int srcImgWidth = srcImg.getWidth(null);
            //获取图片的高
            int srcImgHeight = srcImg.getHeight(null);
            // 加水印
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufImg.createGraphics();
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            //生成二维码
            Image qrImage = getQr(content,130,130);
            //todo 底图上添加二维码
            //将小图片绘到大图片上 .表示你的小图片在大图片上的位置。
            g.drawImage(qrImage, Integer.valueOf(map.get("width")+""), Integer.valueOf(map.get("height")+""), null);
            //设置颜色。
            g.setColor(Color.WHITE);
            g.dispose();
            //todo 上传到七牛云
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bufImg, "jpg", os);
            //为了避免同名覆盖问题,构建新的文件名
            String fileName = UUID.randomUUID().toString() + ".jpg";
            //调用七牛云OSS工具类
            ToolConfig toolConfig = toolConfigService.getToolConfig(BaseConstant.TWO_INT);
            String url = qiniuUtils.uploadByBytes(os.toByteArray(), BaseConstant.PICTURE_URL +fileName,toolConfig);
            //log.info("海报:{}",url);
            return url;
        }catch (Exception e){
            log.info("二维码图片生成失败:{}",e.getMessage());
        }
        return null;
    }

    /**
     * 从文件中读取图片采集参数
     *
     * @param
     * @return
     */
    public Map<String, Object> getPageImg(String code) throws BizException {
        if (pictureMap == null) {
            //为空 从json中读取
            pictureMap = JsonParsingUtils.fileToJsonMap("/json/", "poster.json");
        }
        if (pictureMap.get(code) == null) {
            throw new BizException("采集图片未配置");
        }
        return JSONObject.parseObject(JSONObject.toJSONString(pictureMap.get(code)), Map.class);
    }

    public Image getQr(String content,Integer width,Integer height) throws IOException, WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>(3);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 0);

        ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.L;
        int quietZone = 4;
        if (hints != null) {
            if (hints.containsKey(EncodeHintType.ERROR_CORRECTION)) {
                errorCorrectionLevel = ErrorCorrectionLevel.valueOf(hints.get(EncodeHintType.ERROR_CORRECTION).toString());
            }
            if (hints.containsKey(EncodeHintType.MARGIN)) {
                quietZone = Integer.parseInt(hints.get(EncodeHintType.MARGIN).toString());
            }
        }

        BitMatrix bitMatrix = renderResult(Encoder.encode(content, errorCorrectionLevel, hints), width, height, quietZone);
        MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig(0xFF000001, 0xFFFFFFFF);
        return toBufferedImage(bitMatrix,width,height,matrixToImageConfig);
    }



    public static BufferedImage toBufferedImage(BitMatrix matrix,int width,int height,MatrixToImageConfig config) throws IOException {
        int qrCodeWidth = matrix.getWidth();
        int qrCodeHeight = matrix.getHeight();
        BufferedImage qrCode = new BufferedImage(qrCodeWidth, qrCodeHeight, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < qrCodeWidth; x++) {
            for (int y = 0; y < qrCodeHeight; y++) {
                qrCode.setRGB(x, y, matrix.get(x, y) ? config.getPixelOnColor() : config.getPixelOffColor());
            }
        }

        // 若二维码的实际宽高和预期的宽高不一致, 则缩放
        if (qrCodeWidth != width || qrCodeHeight != height) {
            BufferedImage tmp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            tmp.getGraphics().drawImage(
                    qrCode.getScaledInstance(width, height,
                            java.awt.Image.SCALE_SMOOTH), 0, 0, null);
            qrCode = tmp;
        }

        return qrCode;
    }


    /**
     * 对 zxing 的 QRCodeWriter 进行扩展, 解决白边过多的问题
     * <p/>
     * @param code
     * @param width
     * @param height
     * @param quietZone 取值 [0, 4]
     * @return
     */
    private static BitMatrix renderResult(QRCode code, int width, int height, int quietZone) {
        ByteMatrix input = code.getMatrix();
        if (input == null) {
            throw new IllegalStateException();
        }

        // xxx 二维码宽高相等, 即 qrWidth == qrHeight
        int inputWidth = input.getWidth();
        int inputHeight = input.getHeight();
        int qrWidth = inputWidth + (quietZone * 2);
        int qrHeight = inputHeight + (quietZone * 2);


        // 白边过多时, 缩放
        int minSize = Math.min(width, height);
        int scale = calculateScale(qrWidth, minSize);
        if (scale > 0) {
            int padding, tmpValue;
            // 计算边框留白
            padding = (minSize - qrWidth * scale) / QUIET_ZONE_SIZE * quietZone;
            tmpValue = qrWidth * scale + padding;
            if (width == height) {
                width = tmpValue;
                height = tmpValue;
            } else if (width > height) {
                width = width * tmpValue / height;
                height = tmpValue;
            } else {
                height = height * tmpValue / width;
                width = tmpValue;
            }
        }

        int outputWidth = Math.max(width, qrWidth);
        int outputHeight = Math.max(height, qrHeight);

        int multiple = Math.min(outputWidth / qrWidth, outputHeight / qrHeight);
        int leftPadding = (outputWidth - (inputWidth * multiple)) / 2;
        int topPadding = (outputHeight - (inputHeight * multiple)) / 2;

        BitMatrix output = new BitMatrix(outputWidth, outputHeight);

        for (int inputY = 0, outputY = topPadding; inputY < inputHeight; inputY++, outputY += multiple) {
            // Write the contents of this row of the barcode
            for (int inputX = 0, outputX = leftPadding; inputX < inputWidth; inputX++, outputX += multiple) {
                if (input.get(inputX, inputY) == 1) {
                    output.setRegion(outputX, outputY, multiple, multiple);
                }
            }
        }

        return output;
    }


    /**
     * 如果留白超过15% , 则需要缩放
     * (15% 可以根据实际需要进行修改)
     *
     * @param qrCodeSize 二维码大小
     * @param expectSize 期望输出大小
     * @return 返回缩放比例, <= 0 则表示不缩放, 否则指定缩放参数
     */
    private static int calculateScale(int qrCodeSize, int expectSize) {
        if (qrCodeSize >= expectSize) {
            return 0;
        }

        int scale = expectSize / qrCodeSize;
        int abs = expectSize - scale * qrCodeSize;
        if (abs < expectSize * 0.05) {
            return 0;
        }

        return scale;
    }


   //======================================== 根据url拼接并生成产品海报===========================================//

    public String getProductPoster(String productPictureUrl,String basePictureUrl, String content){
        try {
            // 从 URL 加载图片
            BufferedImage topImage = loadImageFromUrl(productPictureUrl);
            BufferedImage bottomImage = loadImageFromUrl(basePictureUrl);

            Graphics2D g = bottomImage.createGraphics();
            g.drawImage(bottomImage, 0, 0, bottomImage.getWidth(), bottomImage.getHeight(), null);
            //生成二维码
            Image qrImage = getQr(content,900,900);
            //todo 底图上添加二维码
            //将小图片绘到大图片上 .表示你的小图片在大图片上的位置。
            g.drawImage(qrImage, 470, 655, null);
            //设置颜色。
            g.setColor(Color.WHITE);
            g.dispose();
            // 调整下方图片尺寸以匹配上方图片宽度
            bottomImage = resizeImageToMatchWidth(bottomImage, topImage.getWidth());
            // 合并图片
            BufferedImage bufImg = mergeImagesVertically(topImage, bottomImage);
            //todo 上传到七牛云
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bufImg, "jpg", os);
            //为了避免同名覆盖问题,构建新的文件名
            String fileName = UUID.randomUUID().toString() + ".jpg";
            //调用七牛云OSS工具类
            ToolConfig toolConfig = toolConfigService.getToolConfig(BaseConstant.TWO_INT);
            String url = qiniuUtils.uploadByBytes(os.toByteArray(), BaseConstant.PICTURE_URL +fileName,toolConfig);
            log.info("海报图url生成:{},productPictureUrl:{},content:{}",url,productPictureUrl,content);
            return url;
        } catch (Exception e) {
            log.info("生成产品海报图出错:{}",e.getMessage());
        }
        return null;
    }

    /**
     * 按照 二维码格式1 生成推广海报图
     * @param basePictureUrl
     * @param content
     * @return
     */
    @Override
    public String getRegisterQrcodeMap1Url(String basePictureUrl, String content) {
        try {
            // 从 URL 加载图片
            BufferedImage bottomImage = loadImageFromUrl(basePictureUrl);
            Graphics2D g = bottomImage.createGraphics();
            g.drawImage(bottomImage, 0, 0, bottomImage.getWidth(), bottomImage.getHeight(), null);
            //生成二维码
            Image qrImage = getQr(content,260,260);
            //todo 底图上添加二维码
            //将小图片绘到大图片上 .表示你的小图片在大图片上的位置。
            g.drawImage(qrImage, 100, 1500, null);
            //设置颜色。
            g.setColor(Color.WHITE);
            g.dispose();
            // 调整下方图片尺寸以匹配上方图片宽度
            bottomImage = resizeImageToMatchWidth(bottomImage, bottomImage.getWidth());
            //todo 上传到七牛云
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bottomImage, "jpg", os);
            //为了避免同名覆盖问题,构建新的文件名
            String fileName = UUID.randomUUID().toString() + ".jpg";
            //调用七牛云OSS工具类
            ToolConfig toolConfig = toolConfigService.getToolConfig(BaseConstant.TWO_INT);
            String url = qiniuUtils.uploadByBytes(os.toByteArray(), BaseConstant.PICTURE_URL +fileName,toolConfig);
            log.info("推广海报图格式1url生成:{},productPictureUrl:{},content:{}",url,basePictureUrl,content);
            return url;
        } catch (Exception e) {
            log.info("生成推广海报图出错:{}",e.getMessage());
        }
        return null;
    }


    /**
     * 按照 二维码格式3 生成推广海报图
     * @param basePictureUrl
     * @param content
     * @return
     */
    @Override
    public String getRegisterQrcodeMap3Url(String basePictureUrl, String content) {
        try {
            // 从 URL 加载图片
            BufferedImage bottomImage = loadImageFromUrl(basePictureUrl);
            Graphics2D g = bottomImage.createGraphics();
            g.drawImage(bottomImage, 0, 0, bottomImage.getWidth(), bottomImage.getHeight(), null);
            //生成二维码
            Image qrImage = getQr(content,280,280);
            //todo 底图上添加二维码
            //将小图片绘到大图片上 .表示你的小图片在大图片上的位置。
            g.drawImage(qrImage, 270, 815, null);
            //设置颜色。
            g.setColor(Color.WHITE);
            g.dispose();
            // 调整下方图片尺寸以匹配上方图片宽度
            bottomImage = resizeImageToMatchWidth(bottomImage, bottomImage.getWidth());
            //todo 上传到七牛云
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bottomImage, "jpg", os);
            //为了避免同名覆盖问题,构建新的文件名
            String fileName = UUID.randomUUID().toString() + ".jpg";
            //调用七牛云OSS工具类
            ToolConfig toolConfig = toolConfigService.getToolConfig(BaseConstant.TWO_INT);
            String url = qiniuUtils.uploadByBytes(os.toByteArray(), BaseConstant.PICTURE_URL +fileName,toolConfig);
            log.info("推广海报图格式3url生成:{},productPictureUrl:{},content:{}",url,basePictureUrl,content);
            return url;
        } catch (Exception e) {
            log.info("生成推广海报图出错:{}",e.getMessage());
        }
        return null;
    }


    /**
     * 从 URL 加载图片
     */
    private static BufferedImage loadImageFromUrl(String imageUrl) throws IOException {
        try (InputStream in = new URL(imageUrl).openStream()) {
            return ImageIO.read(in);
        }
    }

    /**
     * 调整图片宽度并保持宽高比
     */
    private static BufferedImage resizeImageToMatchWidth(BufferedImage originalImage, int targetWidth) {
        double aspectRatio = (double) originalImage.getHeight() / originalImage.getWidth();
        int targetHeight = (int) (targetWidth * aspectRatio);

        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, originalImage.getType());
        Graphics2D g = resizedImage.createGraphics();
        try {
            // 使用高质量的缩放算法
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        } finally {
            g.dispose();
        }
        return resizedImage;
    }

    /**
     * 垂直合并两张图片
     */
    private static BufferedImage mergeImagesVertically(BufferedImage topImage, BufferedImage bottomImage) {
        int newWidth = topImage.getWidth(); // 两张图片宽度已一致
        int newHeight = topImage.getHeight() + bottomImage.getHeight();
        BufferedImage mergedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = mergedImage.createGraphics();
        try {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, newWidth, newHeight);
            g.drawImage(topImage, 0, 0, null);
            g.drawImage(bottomImage, 0, topImage.getHeight(), null);
        } finally {
            g.dispose();
        }

        return mergedImage;
    }





    //======================================== 根据url拼接并生成产品海报===========================================//

}
