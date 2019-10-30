package com.example.camera.view;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.example.camera.R;
import com.example.camera.utils.GetDpSpUtils;
import com.example.camera.utils.LogUtils;


/**
 * 历史记录查询图表 基于pathMeasure+DashPathEffect+属性动画实现
 *
 * @author zyw
 * @creation 2017-03-06
 */
public class HistoryChartView extends View {

    private String TAG = "HistoryChartView";

    TypedArray ta;

    private float MarginTop = 100;

    private float MarginBottom = 100;

    private float MarginLeft = 100;

    private float MarginRight = 100;

    private float mYLabelSize = 50;

    private float mXlabelSize = 35;

    private float mXUnitTextSize;

    private float mYUnitTextSize;

    // 圆半径
    private int circleRadius = 8;

    private int lineStrokeWidth = 3;

    private int dataStrokeWidth = 3;

    private long duration = 1500;
    private PathMeasure mKcalPathMeasure;
    private PathMeasure mCHOPathMeasure;
    private PathMeasure mProteinPathMeasure;
    private PathMeasure mFatsPathMeasure;
    private PathMeasure mDayKcalPathMeasure;
    private PathMeasure mWeekThresholdPathMeasure;
    private Path mKcalPath;
    private Path mCHOPath;
    private Path mProteinPath;
    private Path mFatsPath;
    private Path mDayKcalPath;
    private Path mWeekThresholdPath;
    //path 作用是从起点到终点移动path画笔并绘制线（moveTo方法只移动path画笔不绘制线），线有直线和曲线。
    /**
     * 柱形绘制进度
     */
    private float mRectFration;

    // X,Y轴的单位长度
    private float Xscale;
    private float Yscale = 20;

    // 绘制X轴总长度
    private float xLength;
    // 绘制Y轴总长度
    private float yLength;

    // X轴第1个节点的偏移位置
    private float xFirstPointOffset;

    // y轴显示的节点间隔距离
    private int yScaleForData = 1;

    // x轴显示的节点间隔距离
    private int xScaleForData = 1;

    // 画线颜色
    private int lineColor;

    private int KcalLineColor;
    private int CHOLineColor;
    private int ProteinLineColor;
    private int FatsLineColor;
    private int mUnitColor;

    private String mXUnitText;
    private String mY1UnitText;
    private String mY2UnitText;

    private int mMode = 1;// 从Activity传过来的模式值 1：天 2：周 3：月 4：年

    // 原点坐标
    private float Xpoint;
    private float Ypoint;

    // X,Y轴上面的显示文字
    private String[] Xlabel = {  "碳水化合物", "蛋白质", "脂肪"};
    private String[] Ylabel = { "0", "600", "1200", "1800", "2400","3000" };
//    private String[] Xlabel = { "热卡", "碳水化合物", "蛋白质", "脂肪"};
//    private String[] Ylabel = { "0", "300", "600", "900", "1200" };
    //private String[] Ylabel2 = { "0", "25", "50", "75", "100" };

    private final static int X_SCALE_FOR_DATA_DAY = 1;
    private final static int X_SCALE_FOR_DATA_WEEK = 1;
    private final static int X_SCALE_FOR_DATA_YEAR = 1;
    private final static int X_SCALE_FOR_DATA_MOUNTH = 5;

    private final static int DAY_MODE = 0;
    private final static int WEEK_MODE = 1;


    // 曲线数据
    private float[] KcalDataArray = { 500, 500, 500, 500, 500, 500, 500 };
    private float[] CHODataArray = { 700, 700, 700, 700, 700, 700, 700 };
    private float[] ProteinDataArray = { 1000, 1000, 1000, 100, 1000, 1000, 1000 };
    private float[] FatsDataArray = { 300, 300, 300, 300, 300, 300, 300 };
    private float[] DayNutriArray={600};
    private float[] DayKcalArray = {1000, 1000, 1000, 100, 1000, 1000, 1000,10000};
    private float[] WeekThresholdArray={1000, 1000, 1000, 100, 1000, 1000, 1000};
    /**
     * 各条柱形图当前top值数组
     */
    private Float[] rectCurrentTops;

    private ValueAnimator mValueAnimator;

    private Paint linePaint;
    private Paint KcalPaint;
    private Paint CHOPaint;
    private Paint ProteinPaint;
    private Paint FatsPaint;
    private Paint DayKcalPaint;
    private Paint WeekThresholdPaint;

    private PathEffect mKcalEffect;//路径效果
    private PathEffect mCHOEffect;
    private PathEffect mProteinEffect;
    private PathEffect mFatsEffect;
    private PathEffect mWeekThsholdEffect;
    private PathEffect mDayKcalEffect;
    //定义一个内存中的图片，该图片将作为缓冲区
    Bitmap mCacheBitmap = null;
    //定义cacheBitmap上的Canvas对象
    Canvas mCacheCanvas = null;

    public HistoryChartView(Context context, String[] xlabel, String[] ylabel,
                            float[] roomDataArray) {
        super(context);
        this.Xlabel = xlabel;
        this.Ylabel = ylabel;
        this.KcalDataArray = roomDataArray;
    }

    public HistoryChartView(Context context, AttributeSet attrs,
                            int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        ta = context
                .obtainStyledAttributes(attrs, R.styleable.HistoryChartView);

        setDefaultAttrrbutesValue();

        initPaint();   //一些画笔 色彩的设置

        initData();

        initParams();

        initPath();

        ta.recycle();
    }

    public HistoryChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HistoryChartView(Context context) {
        this(context, null);
    }

    /**
     * 设置显示数据
     *
     * @param strAlldata
     */
    public void setData(String strAlldata, int mode) {
        // LogUtils.verbose(TAG, "strAlldata = " + strAlldata);
        String[] allHistroyArray = strAlldata.split("-");

        String[] KcalData = {};
        String[] CHOData = {};
        String[] ProteinData = {};
        String[] FatsData = {};
        String[] DayNutriData={};
        String[] DayKcalData={};
        String[] WeekThresholdData = {};
        if(mode==0){//daymode = 0 weekmode=1
        //KcalData = allHistroyArray[0].split(",");
        //CHOData = allHistroyArray[1].split(",");
        //ProteinData = allHistroyArray[2].split(",");
        //FatsData = allHistroyArray[3].split(",");
        DayNutriData=allHistroyArray[0].split(",");
        DayKcalData = allHistroyArray[1].split(",");
        }
        else if(mode==1){
            Log.i(TAG,allHistroyArray[0]);
            Log.i(TAG,allHistroyArray[1]);
            Log.i(TAG,allHistroyArray[2]);
            KcalData = allHistroyArray[0].split(",");
            CHOData = allHistroyArray[1].split(",");
            ProteinData = allHistroyArray[2].split(",");
            FatsData = allHistroyArray[3].split(",");
            Log.i(TAG,"FATS"+allHistroyArray[3]);
            WeekThresholdData = allHistroyArray[4].split(",");
            Log.i(TAG,"WTD"+allHistroyArray[4]);
        }

        mMode = mode;

        initXData(); //初始化x轴 数据
        //initXData(KcalData);
        if(mMode==1){
        initKcalData(KcalData);//将字符串数组 转化为 浮点数 数组    (从nutritionintake中获取的字符串数组Kcaldata----kcaldataarray)

        initCHOData(CHOData);

        initProteinData(ProteinData);

        initFatsData(FatsData);

        initWeekThresholdData(WeekThresholdData);
        }
        else if(mMode==0){
        initDayNutriData(DayNutriData);
        initDayKcalData(DayKcalData);
        }
//        initXData(arrayRoomTempData); //初始化x轴 数据
//
//        initRoomTempData(arrayRoomTempData);
//
//        initTargetTempData(arraySetTempData);
//
//        initPowerOnTimeData(arrayPowerTimeData);
        initData();//初始化数据

        initParams();//初始化宽高比例等数据

        initPath();//初始化路径

         startAnimation();
    }

    private void initPaint() {
        linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);
        linePaint.setColor(lineColor);
        linePaint.setDither(true);
        linePaint.setStrokeWidth(lineStrokeWidth);

        KcalPaint = new Paint();
        KcalPaint.setStyle(Paint.Style.STROKE);//描边
        KcalPaint.setAntiAlias(true);//抗锯齿
        KcalPaint.setColor(KcalLineColor);//设置画笔颜色
        KcalPaint.setDither(true);//会让图像看起来柔和一点。
        KcalPaint.setStrokeWidth(dataStrokeWidth);//画笔样式为空心时，设置空心画笔的宽度

        CHOPaint = new Paint();
        CHOPaint.setStyle(Paint.Style.STROKE);
        CHOPaint.setAntiAlias(true);
        CHOPaint.setColor(CHOLineColor);
        CHOPaint.setDither(true);
        CHOPaint.setStrokeWidth(dataStrokeWidth);
        ProteinPaint = new Paint();
        ProteinPaint.setStyle(Paint.Style.STROKE);
        ProteinPaint.setAntiAlias(true);
        ProteinPaint.setColor(ProteinLineColor);
        ProteinPaint.setDither(true);
        ProteinPaint.setStrokeWidth(dataStrokeWidth);

        FatsPaint = new Paint();
        FatsPaint.setStyle(Paint.Style.STROKE);
        FatsPaint.setAntiAlias(true);
        FatsPaint.setColor(FatsLineColor);
        FatsPaint.setDither(true);
        FatsPaint.setStrokeWidth(dataStrokeWidth);

        WeekThresholdPaint = new Paint();
        WeekThresholdPaint.setStyle(Paint.Style.STROKE);
        WeekThresholdPaint.setAntiAlias(true);
        WeekThresholdPaint.setColor(FatsLineColor);
        WeekThresholdPaint.setDither(true);
        WeekThresholdPaint.setStrokeWidth(dataStrokeWidth);

        DayKcalPaint = new Paint();
        DayKcalPaint.setStyle(Paint.Style.STROKE);
        DayKcalPaint.setAntiAlias(true);
        DayKcalPaint.setColor(FatsLineColor);
        DayKcalPaint.setDither(true);
        DayKcalPaint.setStrokeWidth(dataStrokeWidth);


    }

    /**
     * 初始化数据
     */
    private void initData() {

        mKcalPath = new Path();
        mCHOPath = new Path();
        mProteinPath = new Path();
        mFatsPath = new Path();
        mDayKcalPath = new Path();
        mWeekThresholdPath = new Path();
        mDayKcalPath = new Path();
        rectCurrentTops = new Float[KcalDataArray.length];
    }

    /**
     * 初始化宽高比例等数据
     */
    public void initParams() {
        // LogUtils.error(TAG, "initParams");
        Xpoint = MarginLeft;

        xLength = this.getWidth() - MarginLeft - MarginRight
                - (MarginRight + MarginLeft) / 16;
        yLength = this.getHeight() - MarginTop - MarginBottom;

        Ypoint = this.getHeight() - MarginBottom + mYLabelSize / 3;
        Xscale = (xLength - xFirstPointOffset * 2) / (this.Xlabel.length - 1)* 0.8f;
        Yscale = yLength / (this.Ylabel.length - 1);

    }

    /**
     * 初始化path      似乎折线图才用到
     */
    private void initPath() {
        //initRoomTempPath(KcalDataArray);
        //initTargetTempPath(CHODataArray);
        if(mMode==1){
        initKcalPath(KcalDataArray);
        initCHOPath(CHODataArray);
        initProteinPath(ProteinDataArray);
        Log.i(TAG,"protein"+Float.toString(ProteinDataArray[0]));
        initFatsPath(FatsDataArray);

//        Log.i(TAG,"weekthreshold"+Float.toString(WeekThresholdArray[0]));
        initWeekThresholdPath(WeekThresholdArray);

        }
        if(mMode==0) {
            initDayKcalPath(DayKcalArray);
        }
        //initDayNutriPath(DayNutriArray);
    }

    /**
     * 初始化热量数据
     *
     * @param arrayRoomTempData
     */
    private void initKcalData(String[] KcalData) {
        KcalDataArray = new float[KcalData.length];
        for (int i = 0; i < KcalData.length; i++) {
            if (KcalData[i].length() > 0) {
                KcalDataArray[i] = Float.parseFloat(KcalData[i]);
                // LogUtils.error(TAG, "" + roomTempDataArray[i]);
            }
        }
    }

    /**
     * 初始化设定CHO数据
     *
     * @param arraySetTempData
     */
    private void initCHOData(String[] CHOData) {
        CHODataArray = new float[CHOData.length];
        for (int i = 0; i < CHOData.length; i++) {
            if (CHOData[i].length() > 0) {
                CHODataArray[i] = Float.parseFloat(CHOData[i]);
            }
        }
    }

    /**
     * 初始化蛋白质数据
     *
     * @param arrayPowerTimeData
     */
    private void initProteinData(String[] ProteinData) {
        ProteinDataArray = new float[ProteinData.length];
        for (int i = 0; i < ProteinData.length; i++) {
            if (ProteinData[i].length() > 0) {
                ProteinDataArray[i] = Float.parseFloat(ProteinData[i]);
            }
        }
    }
    private void initFatsData(String[] FatsData) {
        FatsDataArray = new float[FatsData.length];
        for (int i = 0; i < FatsData.length; i++) {
            if (FatsData[i].length() > 0) {
                FatsDataArray[i] = Float.parseFloat(FatsData[i]);
            }
        }
    }
    private void initDayNutriData(String[] NutriData) {
        DayNutriArray = new float[NutriData.length];
        for (int i = 0; i < NutriData.length; i++) {
            if (NutriData[i].length() > 0) {
                DayNutriArray[i] = Float.parseFloat(NutriData[i]);
            }
        }
    }
    private void initDayKcalData(String[] DayKcalData) {
        DayKcalArray = new float[DayKcalData.length];
        for (int i = 0; i < DayKcalData.length; i++) {
            if (DayKcalData[i].length() > 0) {
                DayKcalArray[i] = Float.parseFloat(DayKcalData[i]);
            }
        }
    }
    private void initWeekThresholdData(String[] WeekThresholdData){
        WeekThresholdArray = new float[WeekThresholdData.length];
        for (int i = 0; i < WeekThresholdData.length; i++) {
            if (WeekThresholdData[i].length() > 0) {
                WeekThresholdArray[i] = Float.parseFloat(WeekThresholdData[i]);
            }
        }
    }
    /**
     * 初始化X轴数据
     */
    private void initXData() {
        String nutri[]={  "碳水化合物","     阈值", "   蛋白质","    阈值", "     脂肪","    阈值"};
        //String nutri[]={  "  碳水化合物", "      蛋白质", "        脂肪"};
      //  String nutri[]={ "        热卡", "  碳水化合物", "      蛋白质", "        脂肪"};
        switch (mMode) {
            case DAY_MODE:
                xScaleForData = X_SCALE_FOR_DATA_DAY;
                setXUnitText("营养成分");
                Xlabel = new String[nutri.length];
                for (int i = 0; i < Xlabel.length; i++) {
                    Xlabel[i] = nutri[i];
                }

                //setXUnitText(getResources().getString(R.string.history_x_unit_hour));
                break;
            case WEEK_MODE:
                xScaleForData = X_SCALE_FOR_DATA_WEEK;
                setXUnitText(getResources().getString(R.string.history_x_unit_day));
                //Xlabel = new String[tempData.length];
                Xlabel = new String[7];
                Xlabel[0] = "周日";
                Xlabel[1] = "周一";
                Xlabel[2] = "周二";
                Xlabel[3] = "周三";
                Xlabel[4] = "周四";
                Xlabel[5] = "周五";
                Xlabel[6] = "周六";
//                for (int i = 0; i < Xlabel.length; i++) {
//                Xlabel[i] = Integer.toString(i + 1);
//                }

                //MA里面的一个字符串列表的长度，
                break;
            default:
                break;
        }


//        if(mMode == 2){
//            for (int i = 0; i < Xlabel.length; i++) {
//                Xlabel[i] = nutri[i];
//            }
//         if(mMode == 1)   {
//             Xlabel = new String[tempData.length];
//                 for (int i = 0; i < Xlabel.length; i++) {
//                     Xlabel[i] = "2";
//                 }
//            }
 //       }

    }
//应该是设置表的大小布局
    private void setDefaultAttrrbutesValue() {
        LogUtils.error(TAG, "setDefaultAttrrbutesValue");
        float MarginTopPx = ta.getDimension(
                R.styleable.HistoryChartView_margin_top, 50);
        float MarginBottomPx = ta.getDimension(
                R.styleable.HistoryChartView_margin_bottom, 50);
        float MarginLeftPx = ta.getDimension(
                R.styleable.HistoryChartView_margin_left, 50);
        float MarginRightPx = ta.getDimension(
                R.styleable.HistoryChartView_margin_right, 50);

        float yLabelSizePx = ta.getDimension(
                R.styleable.HistoryChartView_ylabel_text_size, 30);
        float xlabelSizePx = ta.getDimension(
                R.styleable.HistoryChartView_xlabel_text_size, 20);
        float xUnitSizePx = ta.getDimension(
                R.styleable.HistoryChartView_x_unit_text_size, 30);
        float yUnitSizePx = ta.getDimension(
                R.styleable.HistoryChartView_y_unit_text_size, 30);

        float xFirstPointOffsetPx = ta.getDimension(
                R.styleable.HistoryChartView_x_first_point_offset, 30);
        float lineStrokeWidthPx = ta.getDimension(
                R.styleable.HistoryChartView_line_stroke_width, 5);
        float dataStrokeWidthPx = ta.getDimension(
                R.styleable.HistoryChartView_data_stroke_width, 5);
        float circleRadiusPx = ta.getDimension(
                R.styleable.HistoryChartView_circle_radius, 6);

        xFirstPointOffset = GetDpSpUtils.px2sp(getContext(),
                xFirstPointOffsetPx);

        MarginTop = GetDpSpUtils.px2dip(getContext(), MarginTopPx);
        MarginBottom = GetDpSpUtils.px2dip(getContext(), MarginBottomPx);
        MarginLeft = GetDpSpUtils.px2dip(getContext(), MarginLeftPx);
        MarginRight = GetDpSpUtils.px2dip(getContext(), MarginRightPx);

        mYLabelSize = GetDpSpUtils.px2sp(getContext(), yLabelSizePx);
        mXlabelSize = GetDpSpUtils.px2sp(getContext(), xlabelSizePx);

        mXUnitTextSize = GetDpSpUtils.px2sp(getContext(), xUnitSizePx);
        mYUnitTextSize = GetDpSpUtils.px2sp(getContext(), yUnitSizePx);

        lineStrokeWidth = GetDpSpUtils.px2sp(getContext(), lineStrokeWidthPx);
        dataStrokeWidth = GetDpSpUtils.px2sp(getContext(), dataStrokeWidthPx);
        circleRadius = GetDpSpUtils.px2sp(getContext(), circleRadiusPx);

        lineColor = ta.getColor(R.styleable.HistoryChartView_line_color,
                getResources().getColor(R.color.saswell_yellow));
        KcalLineColor = ta.getColor(
                R.styleable.HistoryChartView_first_data_line_color,
                getResources().getColor(R.color.saswell_blue));
        CHOLineColor = ta.getColor(
                R.styleable.HistoryChartView_second_data_line_color,
                getResources().getColor(R.color.saswell_setpoint_temp));

        ProteinLineColor = ta.getColor(
                R.styleable.HistoryChartView_third_data_line_color,
                getResources().getColor(R.color.saswell_protein_temp));

        FatsLineColor = ta.getColor(
                R.styleable.HistoryChartView_forth_data_line_color,
                getResources().getColor(R.color.saswell_dark_grey));

        mUnitColor = ta.getColor(R.styleable.HistoryChartView_unit_color,
                getResources().getColor(R.color.saswell_light_grey));

        mXUnitText = ta.getString(R.styleable.HistoryChartView_x_unit_text);
        mY1UnitText = ta.getString(R.styleable.HistoryChartView_y1_unit_text);
       // mY2UnitText = ta.getString(R.styleable.HistoryChartView_y2_unit_text);
    }

    /**
     * 设置X轴单位符号
     *
     * @param xUnit
     */
    public void setXUnitText(String xUnit) {
        mXUnitText = xUnit;
    }
//就叫 营养成分
    /**
     * 绘制单位符号
     *
     * @param canvas
     */
    private void drawUnit(Canvas canvas) {
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setStrokeWidth(dataStrokeWidth);
        p.setColor(mUnitColor);

        drawXUnit(canvas, p);
        drawY1Unit(canvas, p);
//        drawY2Unit(canvas, p);
    }

    // 画横轴
    private void drawXLine(Canvas canvas, Paint p) {
        p.setColor(getResources().getColor(R.color.saswell_yellow));
        canvas.drawLine(Xpoint, Ypoint, xLength + MarginLeft, Ypoint, p); //起点和终点的xy坐标，画笔p
    }

    // 画灰色横轴
    private void drawGreyXLine(Canvas canvas, Paint p) {
        p.setColor(getResources().getColor(R.color.saswell_grey_line));
        float startX = Xpoint + MarginLeft / 4+30;
        //横轴加了30
        // 纵向
        for (int i = yScaleForData; (yLength - i * Yscale) >= 0; i += yScaleForData) {
            float startY = Ypoint - i * Yscale;
            canvas.drawLine(startX - MarginLeft / 4, startY, xLength
                    + MarginLeft, startY, p);
        }
    }

    // 画折线图
    private void drawData(Canvas canvas, float[] data, int dataColor) {
        Paint p = new Paint();
        p.setAntiAlias(true);//抗锯齿。。 nb
        p.setStrokeWidth(dataStrokeWidth);
        p.setTextSize(mXlabelSize);
        // 横向private String[] Xlabel = { "热卡", "碳水化合物", "蛋白质", "脂肪"};
        for (int i = 0; i < Xlabel.length; i++) {
            int xLableInt = i;
           // int xLableInt = Integer.parseInt(Xlabel[i]);
            float startX = Xpoint + i * Xscale + xFirstPointOffset;
            if (xLableInt % xScaleForData == 0) {
                p.setColor(lineColor);
                canvas.drawText(this.Xlabel[i], startX - mXlabelSize / 3,
                        Ypoint + mXlabelSize * 3 / 2, p);
            }
            p.setColor(dataColor);
            canvas.drawCircle(startX, getDataY(data[i], Ylabel), circleRadius,
                    p);
        }

        p.setTextSize(mYLabelSize);
        // 纵向
        for (int i = 0; (yLength - i * Yscale) >= 0; i += yScaleForData) {
            p.setColor(lineColor);
            canvas.drawText(this.Ylabel[i], MarginLeft / 4,
                    getDataY(Float.valueOf(Ylabel[i]), Ylabel) + mYLabelSize
                            / 3, p);
           // canvas.drawText(this.Ylabel2[i], this.getWidth() - MarginLeft,
            //        getDataY(Float.valueOf(Ylabel2[i]), Ylabel2) + mYLabelSize
             //               / 3, p);
        }
    }

    // 获取room temp绘线Path数据
    private void initKcalPath(float[] data) {
        mKcalPath.reset();
        // Path path = new Path();
        float pointX;
        float pointY;
        // 横向
        mKcalPath.moveTo(Xpoint + xFirstPointOffset,
                getDataY(data[0], Ylabel));
        mKcalPath.moveTo(Xpoint + xFirstPointOffset,
                getDataY(data[0], Ylabel));
        for (int i = 0; i < Xlabel.length; i++) {
            float startX = Xpoint + i * Xscale + xFirstPointOffset;
            // 绘制数据连线
            if (i != 0) {
                pointX = Xpoint + (i - 1) * Xscale + xFirstPointOffset;
                pointY = getDataY(data[i - 1], Ylabel);
                mKcalPath.lineTo(pointX, pointY);
            }
            if (i == Xlabel.length - 1) {
                pointX = startX;
                pointY = getDataY(data[i], Ylabel);
                mKcalPath.lineTo(pointX, pointY);
            }
        }
        mKcalPathMeasure = new PathMeasure( mKcalPath, false);
    }

    /**
     * 获取target temp绘线Path数据
     *
     * @param data
     */
    private void initCHOPath(float[] data) {
        mCHOPath.reset();
        float pointX;
        float pointY;
        // 横向
        mCHOPath.moveTo(Xpoint + xFirstPointOffset,
                getDataY(data[0], Ylabel));
        for (int i = 0; i < Xlabel.length; i++) {
            float startX = Xpoint + i * Xscale + xFirstPointOffset;
            // 绘制数据连线
            if (i != 0) {
                pointX = Xpoint + (i - 1) * Xscale + xFirstPointOffset;
                pointY = getDataY(data[i - 1], Ylabel);
                mCHOPath.lineTo(pointX, pointY);
            }
            if (i == Xlabel.length - 1) {
                pointX = startX;
                pointY = getDataY(data[i], Ylabel);
                mCHOPath.lineTo(pointX, pointY);
            }
        }
        mCHOPathMeasure = new PathMeasure(mCHOPath, false);
    }
    private void initProteinPath(float[] data) {
        mProteinPath.reset();
        // Path path = new Path();
        float pointX;
        float pointY;
        // 横向
        mProteinPath.moveTo(Xpoint + xFirstPointOffset,
                getDataY(data[0], Ylabel));
        for (int i = 0; i < Xlabel.length; i++) {
            float startX = Xpoint + i * Xscale + xFirstPointOffset;
            // 绘制数据连线
            if (i != 0) {
                pointX = Xpoint + (i - 1) * Xscale + xFirstPointOffset;
                pointY = getDataY(data[i - 1], Ylabel);
                mProteinPath.lineTo(pointX, pointY);
            }
            if (i == Xlabel.length - 1) {
                pointX = startX;
                pointY = getDataY(data[i], Ylabel);
                mProteinPath.lineTo(pointX, pointY);
            }
        }
        mProteinPathMeasure = new PathMeasure( mProteinPath, false);
    }
    private void initFatsPath(float[] data) {
        mFatsPath.reset();
        // Path path = new Path();
        float pointX;
        float pointY;
        // 横向
        mFatsPath.moveTo(Xpoint + xFirstPointOffset,
                getDataY(data[0], Ylabel));
        for (int i = 0; i < Xlabel.length; i++) {
            float startX = Xpoint + i * Xscale + xFirstPointOffset;
            // 绘制数据连线
            if (i != 0) {
                pointX = Xpoint + (i - 1) * Xscale + xFirstPointOffset;
                pointY = getDataY(data[i - 1], Ylabel);
                mFatsPath.lineTo(pointX, pointY);
            }
            if (i == Xlabel.length - 1) {
                pointX = startX;
                pointY = getDataY(data[i], Ylabel);
                mFatsPath.lineTo(pointX, pointY);
            }
        }
        mFatsPathMeasure = new PathMeasure( mFatsPath, false);
    }
    private void initWeekThresholdPath(float[] data){
        mWeekThresholdPath.reset();
        // Path path = new Path();
        float pointX;
        float pointY;
        // 横向
        mWeekThresholdPath.moveTo(Xpoint + xFirstPointOffset,
                getDataY(data[0], Ylabel));
        for (int i = 0; i < Xlabel.length; i++) {
            float startX = Xpoint + i * Xscale + xFirstPointOffset;
            // 绘制数据连线
            if (i != 0) {
                pointX = Xpoint + (i - 1) * Xscale + xFirstPointOffset;
                pointY = getDataY(data[i - 1], Ylabel);
                mWeekThresholdPath.lineTo(pointX, pointY);
            }
            if (i == Xlabel.length - 1) {
                pointX = startX;
                pointY = getDataY(data[i], Ylabel);
                mWeekThresholdPath.lineTo(pointX, pointY);
            }
        }
        mWeekThresholdPathMeasure = new PathMeasure( mWeekThresholdPath, false);
    }
    private void initDayKcalPath(float[] data) {
        mDayKcalPath.reset();
        // Path path = new Path();
        float pointX;
        float pointY;
        // 横向
        mDayKcalPath.moveTo(Xpoint + xFirstPointOffset,
                getDataY(data[0], Ylabel));
        for (int i = 0; i < Xlabel.length; i++) {
            float startX = Xpoint + i * Xscale + xFirstPointOffset;
            // 绘制数据连线
            if (i != 0) {
                pointX = Xpoint + (i - 1) * Xscale + xFirstPointOffset;
                pointY = getDataY(data[i - 1], Ylabel);
                mDayKcalPath.lineTo(pointX, pointY);
            }
            if (i == Xlabel.length - 1) {
                pointX = startX;
                pointY = getDataY(data[i], Ylabel);
                mDayKcalPath.lineTo(pointX, pointY);
            }
        }
        mDayKcalPathMeasure = new PathMeasure( mDayKcalPath, false);
    }

    // 绘制矩形图
    private void drawRect(Canvas canvas, float[] data, int dataColor) {
        Paint p = new Paint();
        float left;
        float top;
        float right;
        float bottom;
        float stopY = getDataY(Float.parseFloat(Ylabel[Ylabel.length - 1]),
                Ylabel);// 灰色线Y轴位置
        float rectYScale;

        p.setAntiAlias(true);
        p.setStrokeWidth(dataStrokeWidth);
        p.setColor(dataColor);

        // 横向
        p.setTextSize(mXlabelSize);
        for (int i = 0; i < Xlabel.length; i++) {
            // 绘制柱形图
            int xLableInt = i;
            left = Xpoint + i * Xscale + xFirstPointOffset + Xscale / 6; //x最小 左侧
            top = getDataY(data[i],Ylabel);
           // top = Ypoint - rectYScale + lineStrokeWidth;// 要绘制的rect最终top值 y最大，下侧
            // 起点top + (起点top - 终点top) * mRectFration
            rectCurrentTops[i] = Ypoint - (Ypoint - top) * mRectFration;// 根据fraction动态更新top值
            right = left + Xscale * 4 / 6;
            bottom = Ypoint;
            //绘制柱状图的数据  CHO PROTEIN...
            canvas.drawRect(left,rectCurrentTops[i], right, bottom, p);// 每次valueAnimator更新时重绘最新top值
            float startX = Xpoint + i * Xscale + xFirstPointOffset;
            if (xLableInt % xScaleForData == 0) {
                p.setColor(lineColor);
                canvas.drawText(this.Xlabel[i], startX - mXlabelSize / 3,
                        Ypoint + mXlabelSize * 3 / 2, p);
            }
        }

        //纵向
        p.setTextSize(mYLabelSize);
        for (int i = 0; (yLength - i * Yscale) >= 0; i += yScaleForData) {
            p.setColor(lineColor);
            canvas.drawText(this.Ylabel[i], MarginLeft / 4,
                    getDataY(Float.valueOf(Ylabel[i]), Ylabel) + mYLabelSize
                            / 3, p);
        }

    }

    private void drawY1Unit(Canvas canvas, Paint p) {
        int maxYLabelValue = Integer.valueOf(Ylabel[Ylabel.length - 1]);
        p.setTextSize(mYUnitTextSize);
        float textWidth = p.measureText(mY1UnitText);
        canvas.drawText(mY1UnitText, MarginLeft / 2 - textWidth / 2,
                getDataY(maxYLabelValue, Ylabel) - mYLabelSize - mYLabelSize
                        / 5, p);
    }

//    private void drawY2Unit(Canvas canvas, Paint p) {
//        int maxYLabel2Value = Integer.valueOf(Ylabel2[Ylabel2.length - 1]);
//        p.setTextSize(mYUnitTextSize);
//        float textWidth = p.measureText(mY2UnitText);
//        canvas.drawText(mY2UnitText, this.getWidth() - MarginRight / 2
//                - textWidth * 3 / 4, getDataY(maxYLabel2Value, Ylabel2)
//                - mYLabelSize - mYLabelSize / 5, p);
//    }

    private void drawXUnit(Canvas canvas, Paint p) {
        p.setTextSize(mXUnitTextSize);
        float textWidth = p.measureText(mXUnitText);
        canvas.drawText(mXUnitText, this.getWidth() / 2 - textWidth / 2, Ypoint
                + mXlabelSize * 3 + mXlabelSize / 5, p);
    }

    /**
     * 获取data对应绘制Y点值
     */
    private float getDataY(float dataY, String[] Ylabel) {
        float y0 = 0;
        float y1 = 0;
        try {
            y0 = Float.parseFloat(Ylabel[0]);
            y1 = Float.parseFloat(Ylabel[1]);
        } catch (Exception e) {
            return 0;
        }
        try {
            return Ypoint - ((dataY - y0) * Yscale / (y1 - y0));
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // LogUtils.error(TAG, "onLayout");
        initParams();

        if (onViewLayoutListener != null) {
            onViewLayoutListener.onLayoutSuccess();
        }

        //创建一个与该View相同大小的缓冲区
        mCacheBitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
        mCacheCanvas = new Canvas();
        //设置cacheCanvas将会绘制到内存中cacheBitmap上
        mCacheCanvas.setBitmap(mCacheBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCacheCanvas.drawColor(Color.BLACK);

        drawGreyXLine(mCacheCanvas, linePaint);

        drawUnit(mCacheCanvas);
        if(mMode ==0){
            if (ProteinDataArray.length > 1) {
                //drawRect(mCacheCanvas, ProteinDataArray, ProteinLineColor);
                drawRect(mCacheCanvas, DayNutriArray, ProteinLineColor);
            }
//            if(DayKcalArray.length>1){
//                drawData(mCacheCanvas,DayKcalArray,KcalLineColor);
//            }
//            mCacheCanvas.drawPath(mDayKcalPath,DayKcalPaint);

        }
        if(mMode ==1){
            if (KcalDataArray.length > 1) {
                drawData(mCacheCanvas, KcalDataArray, KcalLineColor);
            }
            mCacheCanvas.drawPath( mKcalPath, KcalPaint);

            if (CHODataArray.length > 1) {
                drawData(mCacheCanvas, CHODataArray, CHOLineColor);
            }
            mCacheCanvas.drawPath(mCHOPath, CHOPaint);

            if (ProteinDataArray.length > 1) {
                drawData(mCacheCanvas, ProteinDataArray, ProteinLineColor);
            }
            mCacheCanvas.drawPath( mProteinPath, ProteinPaint);

            if (FatsDataArray.length > 1) {
                drawData(mCacheCanvas, FatsDataArray, FatsLineColor);
            }
            mCacheCanvas.drawPath( mFatsPath, FatsPaint);

            if(DayKcalArray.length>1){
                drawData(mCacheCanvas,DayKcalArray,FatsLineColor);
            }
            mCacheCanvas.drawPath(mDayKcalPath,DayKcalPaint);

            if(WeekThresholdArray.length>1){
                drawData(mCacheCanvas,WeekThresholdArray,KcalLineColor);
            }
            mCacheCanvas.drawPath(mWeekThresholdPath,WeekThresholdPaint);
        }
        drawXLine(mCacheCanvas, linePaint);

        Paint bmpPaint = new Paint();
        //将cacheBitmap绘制到该View组件
        canvas.drawBitmap(mCacheBitmap, 0, 0, bmpPaint);
    }

    private void startAnimation() {
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
        }
        final float targetTempLength = mKcalPathMeasure.getLength();
        final float roomTempLength = mCHOPathMeasure.getLength();
        final float ProteinLength = mProteinPathMeasure.getLength();
        final float FatsLength = mFatsPathMeasure.getLength();
        final float DayKcalLength = mDayKcalPathMeasure.getLength();
        final float WeekThsholdLength = mWeekThresholdPathMeasure.getLength();
        mValueAnimator = ValueAnimator.ofFloat(1, 0);
        mValueAnimator.setDuration(duration);
        // 减速插值器
        mValueAnimator.setInterpolator(new DecelerateInterpolator());
        ///////////////////折线图的动画效果  就是一点一点的画出来折线
        mValueAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = (Float) animation.getAnimatedValue();
                // 更新mCHOEffect
                mKcalEffect = new DashPathEffect(new float[] {
                        targetTempLength, targetTempLength }, fraction
                        * targetTempLength);
                KcalPaint.setPathEffect(mKcalEffect);
                // 更新mKcalEffect
                mCHOEffect = new DashPathEffect(new float[] {
                        roomTempLength, roomTempLength }, fraction
                        * roomTempLength);
                CHOPaint.setPathEffect(mCHOEffect);
                mProteinEffect = new DashPathEffect(new float[]{
                        ProteinLength,ProteinLength},
                        fraction *ProteinLength);
                ProteinPaint.setPathEffect(mProteinEffect);

                mFatsEffect = new DashPathEffect(new float[]{
                        FatsLength,FatsLength},
                        fraction *FatsLength);
                FatsPaint.setPathEffect(mFatsEffect);

                mDayKcalEffect = new DashPathEffect(new float[]{
                        DayKcalLength,DayKcalLength},
                                fraction*DayKcalLength);
                DayKcalPaint.setPathEffect(mDayKcalEffect);


                mWeekThsholdEffect = new DashPathEffect(new float[]{
                    WeekThsholdLength, WeekThsholdLength},
                        fraction* WeekThsholdLength);
                WeekThresholdPaint.setPathEffect(mWeekThsholdEffect);


                // 更新rect绘制fraction进度
                mRectFration = 1 - fraction;// fraction是1->0 我们需要的柱形图绘制比例是0->1
                //postInvalidate();
                invalidate();
            }
        });
        mValueAnimator.start();
    }

    public interface OnViewLayoutListener {
        public void onLayoutSuccess();
    }

    public void setOnViewLayoutListener(
            OnViewLayoutListener onViewLayoutListener) {
        this.onViewLayoutListener = onViewLayoutListener;
    }

    private OnViewLayoutListener onViewLayoutListener;
}
