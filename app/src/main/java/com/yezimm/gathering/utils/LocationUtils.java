package com.yezimm.gathering.utils;


import android.location.Location;

import com.sogou.map.mobile.engine.utils.CoordinateConvertor;
import com.sogou.map.mobile.geometry.Coordinate;

import org.json.JSONException;

public class LocationUtils {
    /**
     * 经纬度转换为地理坐标
     * @param location
     * @return 注意x,y表示地理坐标 z表示方向
     */
    public static Coordinate convertLocationToCoor(Location location) {
        if(location != null){
            double lon = location.getLongitude(), lat = location.getLatitude();
            double bearing = location.getBearing();
            Coordinate point = convertLocationToCoor(lon,lat);
            point.setZ((float)bearing);
            return point;
        } else {
            return null;
        }
    }
    /**
     * 经纬度转换为地理坐标
     * @param lon
     * @param lat
     * @return
     */
    public static Coordinate convertLocationToCoor(double lon,double lat) {
        double[] mer = CoordinateConvertor.LL2Mer(lon, lat);
        Coordinate pnt = new Coordinate((float)mer[0], (float)mer[1], 0);
        return pnt;
    }

    /**
     * 将墨卡托坐标转换成经纬度
     *
     * @param coord
     * @return 返回的第一个参数是经度，第二个参数是维度
     */
    public static double [] getLocationInfo(Coordinate coord) {
        if (coord == null)
            return null;
        double[] ll = CoordinateConvertor.Mer2LL(coord.getX(), coord.getY());
        if (ll.length < 2)
            return null;

        return ll;
    }

    /**
     * 解析定位请求返回结果
     * @param json：json格式定位请求返回结果
     * @return
     * 目前逻辑为：全国不返回“全国”，
     * 返回内容中的error为0 且没有prov和city节点时，则为全国  如{"error":"0"}
     * 结果为城市时,error为0，prov和city不为空，如{"prov":{"c":[12722500,3556031,7],"name":"湖北"},"error":"0","city":{"c":[12483812,3746531,12],"name":"襄阳"}}
     * 省份为台北地区没有city返回,城市为北京 没有prov返回
     * Error不为0时，表示出现异常，error的内容为异常原因。
     * @throws JSONException
     */
//    public static Place parsePlace(String json) throws JSONException {
//        if (json == null)
//            return null;
//
//        JSONObject jsonObj = new JSONObject(json);
//        String err = jsonObj.optString("error");
//        if (err != null && !err.equals("0"))
//            return null;
//
//        JSONObject proObj = jsonObj.optJSONObject("prov");
//        JSONObject cityObj = jsonObj.optJSONObject("city");
//
//        if(cityObj != null){
//            String cityName = cityObj.optString("name");
//            if(!NullUtils.isNull(cityName)){
//                City city = new City();
//                city.setName(cityName);
//                return city;
//            }
//        } else if(proObj != null){
//            String proName = proObj.optString("name");
//            if(!NullUtils.isNull(proName)){
//                Province province = new Province();
//                province.setName(proName);
//                return province;
//            }
//        } else {
//            return new Country();
//        }
//        return null;
//    }

    public static  float getDegree(Coordinate preCoord ,Coordinate cuurentCoord ){
        float x1 = preCoord.getX(),x2 = cuurentCoord.getX(); //点1坐标;
        float y1= preCoord.getY(),y2 = cuurentCoord.getY(); //点2坐标
        float x = x2 - x1;
        float y = y2 - y1;
        float degree = (float) (Math.PI / 2 -  Math.atan2(y, x));
        if(degree < 0){
            degree += (2 * Math.PI) ;
        }
        return (float) (degree * (180 / Math.PI));
    }

    public static  float getDegree(double prex, double prey,double curx, double cury){
        float x1 = (float) prex, x2 = (float) curx; //点1坐标;
        float y1= (float) prey,y2 = (float) cury; //点2坐标
        float x = x2 - x1;
        float y = y2 - y1;
        float degree = (float) (Math.PI / 2 -  Math.atan2(y, x));
        if(degree < 0){
            degree += (2 * Math.PI) ;
        }
        return (float) (degree * (180 / Math.PI));
        //    float degree = (float) Math.atan2(y , x);
        //    return (float) (degree * (180 / Math.PI));
    }

    public static double[] wgs84ToSogou(double lon, double lat) {
        return WGS84MecatorTransform.MercatorLLToProj(lon, lat);
    }

    public static double[] sogouToWGS84(double lon, double lat) {
        return WGS84MecatorTransform.MercatorProjToLL(lon, lat);
    }

    public static double[] wgs84ToGcj02(double lon, double lat) {
        return WGSGCJ02Transform.transformGS2MGS(lon, lat);
    }

    public static double[] gcj02ToWGS84(double lon, double lat) {
        return WGSGCJ02Transform.transformMGS2GS(lon, lat);
    }

    public static double[] wgs84ToBaidu(double lon, double lat) {
        double[] gxy = WGSGCJ02Transform.transformGS2MGS(lon, lat);
        return GCJ02BD09Transform.bd_encrypt(gxy[0], gxy[1]);
    }

    public static double[] baiduToWGS84(double lon, double lat) {
        double[] gxy = GCJ02BD09Transform.bd_decrypt_reach(lon, lat);
        return WGSGCJ02Transform.transformMGS2GS(gxy[0], gxy[1]);
    }

    public static void main(String[] args) {
        double[] wgsxy = { 116.32664812d, 39.91505345d };
//      double[] wgsxy = {116.32162388, 40.06641628};
        System.out.format("orig wgs\t%.8f\t%.8f\n", wgsxy[0], wgsxy[1]);

        double[] sogouxy = LocationUtils.wgs84ToSogou(wgsxy[0], wgsxy[1]);
        System.out.format("wgs-sogou\t%.8f\t%.8f\n", sogouxy[0], sogouxy[1]);

        wgsxy = LocationUtils.sogouToWGS84(sogouxy[0], sogouxy[1]);
        System.out.format("sogou-wgs\t%.8f\t%.8f\n", wgsxy[0], wgsxy[1]);

        double[] gcjxy = LocationUtils.wgs84ToGcj02(wgsxy[0], wgsxy[1]);
        System.out.format("wgs-gcj\t%.8f\t%.8f\n", gcjxy[0], gcjxy[1]);

        wgsxy = LocationUtils.gcj02ToWGS84(gcjxy[0], gcjxy[1]);
        System.out.format("gcj-wgs\t%.8f\t%.8f\n", wgsxy[0], wgsxy[1]);

        double[] baiduxy = LocationUtils.wgs84ToBaidu(wgsxy[0], wgsxy[1]);
        System.out.format("wgs-baidu\t%.8f\t%.8f\n", baiduxy[0], baiduxy[1]);

        wgsxy = LocationUtils.baiduToWGS84(baiduxy[0], baiduxy[1]);
        System.out.format("baidu-wgs\t%.8f\t%.8f\n", wgsxy[0], wgsxy[1]);
    }
}


///////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////
class ReachTransform {

    static interface ReversTransform {
        double[] transform(double x, double y);
    }

    /*** 2 * pi * 6378245.0 / 360 * 1.0e-8 = 1mm **/
    final static double eps = 1.0e-8;

    /*** speed insurance **/
    final static int max_trys = 8;

    public static double[] transform(double lon, double lat, ReversTransform revTrans) {
        // // for analysis:
        // double axy0[] = { lon, lat };

        // double[] qxy1 = transformGS2MGS(axy0[0], axy0[1]);
        // double axy1[] = { lon + axy0[0] - qxy1[0], lat + axy0[1] - qxy1[1] };
        //
        // double[] qxy2 = transformGS2MGS(axy1[0], axy1[1]);
        // double axy2[] = { lon + axy1[0] - qxy2[0], lat + axy1[1] - qxy2[1] };
        //
        // double[] qxy3 = transformGS2MGS(axy2[0], axy2[1]);
        // double axy3[] = { lon + axy2[0] - qxy3[0], lat + axy2[1] - qxy3[1] };
        //
        // double[] qxy4 = transformGS2MGS(axy3[0], axy3[1]);
        // double axy4[] = { lon + axy3[0] - qxy4[0], lat + axy3[1] - qxy4[1] };
        //
        // return axy4;

        double qxy[] = { 0.0d, 0.0d };
        double dLat = 0.0d, dLon = 0.0d;
        double[] axy = { lon, lat };

        int tries = 0;
        do {
            qxy = revTrans.transform(axy[0], axy[1]);
            dLon = lon - qxy[0];
            dLat = lat - qxy[1];
            axy[0] += dLon;
            axy[1] += dLat;
        } while (++tries < max_trys && (Math.abs(dLat) > eps || Math.abs(dLon) > eps));

        return axy;
    }
}

/*** GCJ02 <--> BD09 transfromation **/
class GCJ02BD09Transform {

    final static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

    /*** gcj02 --> bd09 **/
    public static double[] bd_encrypt(double gg_lon, double gg_lat) {
        double x = gg_lon, y = gg_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        return new double[] { z * Math.cos(theta) + 0.0065, z * Math.sin(theta) + 0.006 };
    }

    /*** bd09 --> gcj02 **/
    public static double[] bd_decrypt(double bd_lon, double bd_lat) {
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        return new double[] { z * Math.cos(theta), z * Math.sin(theta) };
    }

    /*** bd09 --> gcj02 use the reach approach **/
    public static double[] bd_decrypt_reach(double bd_lon, double bd_lat) {
        return ReachTransform.transform(bd_lon, bd_lat, new ReachTransform.ReversTransform() {
            public double[] transform(double lon, double lat) {
                return bd_encrypt(lon, lat);
            }
        });
    }

}


/*** GCJ02(mars) <--> wgs transfromation **/
class WGSGCJ02Transform {
    static final double pi = 3.14159265358979324;

    // Krasovsky 1940; a = 6378245.0; 1/f = 298.3; b = a * (1 - f);
    static final double a = 6378245.0;

    // ee = (a^2 - b^2) / a^2;
    static final double ee = 0.00669342162296594323;

    // Mars Geodetic System World ==> Geodetic System; cost 3e-3 ms
    public static double[] transformMGS2GS(double mgLon, double mgLat) {

        return ReachTransform.transform(mgLon, mgLat, new ReachTransform.ReversTransform() {
            public double[] transform(double lon, double lat) {
                return transformGS2MGS(lon, lat);
            }
        });
    }

    // World Geodetic System ==> Mars Geodetic System; cost 8e-4 ms
    public static double[] transformGS2MGS(double wgLon, double wgLat) {
        if (outOfChina(wgLon, wgLat))
            return new double[] { wgLon, wgLat };
        double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
        double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
        double radLat = wgLat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        return new double[] { wgLon + dLon, wgLat + dLat };
    }

    static boolean outOfChina(double lon, double lat) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }

    static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
        return ret;
    }
}

/*** mercator <--> wgs transfromation **/
class WGS84MecatorTransform {

    static final double PI = 3.14159265358979323846;
    static final double COORD_MIN = -1000000000;
    static final double COORD_MAX = 1000000000;
    /*** min decimal exponent **/
    static final int DBL_MIN_10_EXP = -307;
    /*** max decimal exponent **/
    static final int DBL_MAX_10_EXP = 308;

    static double DEG_TO_RAD(double a) {
        return ((a) * PI / 180.0);
    }

    static double RAD_TO_DEG(double a) {
        return ((a) * 180.0 / PI);
    }

    static double CLIP(double x, double min, double max) {
        return ((x) < (min) ? (min) : ((x) > (max) ? (max) : (x)));
    }

    /** Note: We make sure that the X and Y coordinates are within proper bounds. **/
    /** This ensures that we don't get a PLOSS error when we use pDest in sin **/
    /** and cos, etc. **/
    static double[] DegreesToRadians(double x, double y) {
        return new double[]{ DEG_TO_RAD(CLIP(x, -360.0, 360.0)), DEG_TO_RAD(CLIP(y, -90.0, 90.0))};
    }

    static double[] RadiansToDegrees(double x, double y) {
        return new double[]{CLIP(RAD_TO_DEG(x), -360.0, 360.0), CLIP(RAD_TO_DEG(y), -90.0, 90.0)};
    }


    /** Convert a point from Long/Lat to Mercator coordinates. **/
    public static double[] MercatorLLToProj(double x, double y) {
        double sinphi, esinphi, temp1, temp2;

        double[] pDest = DegreesToRadians(x, y);
        /** See equation 7-6 on page 44. **/
        pDest[0] = 6378206.4 * pDest[0];

        /** See equation 7-7a on page 44. We have to avoid dividing by zero or **/
        /** taking log(0), so we use COORD_MAX and COORD_MIN to represent **/
        /** infinity and negative infinity, respectively. We don't need to **/
        /** worry about esinphi == 1 or -1, because e is always < 1. **/
        sinphi = Math.sin(pDest[1]);
        if ((temp1 = 1 + sinphi) == 0.0)
            pDest[1] = (double) COORD_MIN;
        else if ((temp2 = 1 - sinphi) == 0.0)
            pDest[1] = (double) COORD_MAX;
        else {
            esinphi = 0.082271854224939184 * sinphi;
            pDest[1] = 3189103.2 * Math.log(temp1 / temp2
                    * Math.pow((1 - esinphi) / (1 + esinphi), 0.082271854224939184));
        }
        return pDest;
    }

    /** Convert a point from Mercator coordinates to Long/Lat. **/
    public static double[] MercatorProjToLL(double x, double y) {
        double temp, chi, chi2, coschi2;
        double[] pDest = {0, 0};
        /** See equation 7-12 on page 45. **/
        pDest[0] = x / 6378206.4;

        /** See equations 7-10 and 7-13 on pages 44 and 45. We don't need to **/
        /** worry about dividing by zero, because pSys->EllipsoidInfo.a cannot **/
        /** be zero. However, we do need to worry about overflow and underflow **/
        /** errors when calling exp(). **/
        temp = -y / 6378206.4;
        if (temp < (double) DBL_MIN_10_EXP)
            chi = PI / 2;
        else if (temp > (double) DBL_MAX_10_EXP)
            chi = -PI / 2;
        else
            chi = PI / 2 - 2 * Math.atan(Math.exp(temp));

        /** See equation 3-5 on page 45. To speed up the equation, we use the **/
        /** procedure described on page 19. See equation 3-35 on page 19. **/
        chi2 = 2 * chi;
        coschi2 = Math.cos(chi2);
        pDest[1] = chi + Math.sin(chi2) * (0.0033938814110493522 + coschi2 * (1.3437644537757259e-005
                + coschi2 * (7.2964865099246009e-008 + coschi2 * 4.4551470401894685e-010)));
        return RadiansToDegrees(pDest[0], pDest[1]);
    }
}
