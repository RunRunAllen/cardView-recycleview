package com.example.cardview;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.CopyObjectResult;
import com.alibaba.sdk.android.oss.model.DeleteObjectResult;
import com.alibaba.sdk.android.oss.model.OSSRequest;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.alibaba.sdk.android.oss.model.ResumableUploadRequest;
import com.alibaba.sdk.android.oss.model.ResumableUploadResult;

import java.io.File;
import java.util.ArrayList;

public class OssService {
    private OSSClient mOss;
    private String mBucket;

//
//    /**
//     *
//     * @param oss       oss实例对象
//     * @param bucket    bucket源存储空间
//     */
//    public OssService(OSSClient oss, String bucket) {
//        this.mOss = oss;
//        this.mBucket = bucket;
//    }
//
//
//    /**
//     * 获取指定阿里oss的bucket中时候是否存在对应名称的文件对象
//     *
//     * @param objectKey  对象在阿里服务器的存储路径
//     * @return true      文件已存在
//     */
//    public boolean isObjectExit(String objectKey) {
//        boolean fileExist = false;
//        try {
//            if (mOss.doesObjectExist(mBucket, objectKey)) {
//                Log.d("doesObjectExist", "object exist.");
//                fileExist = true;
//            } else {
//                Log.d("doesObjectExist", "object does not exist.");
//            }
//        } catch (ClientException e) {
//            // 本地异常如网络异常等
//            e.printStackTrace();
//        } catch (ServiceException e) {
//            // 服务异常
//            Log.e("ErrorCode", e.getErrorCode());
//            Log.e("RequestId", e.getRequestId());
//            Log.e("HostId", e.getHostId());
//            Log.e("RawMessage", e.getRawMessage());
//        }
//
//        return fileExist;
//    }
//
//
//    /**
//     * 常规单个文件上传，请注意如果文件过大（相对网速考虑），请考虑使用
//     *
//     * @param filePath                  本地文件存储路径
//     * @param imageUpLoadCallback       回调
//     * @param errRetry                  出错重试的最多次数
//     */
//    public void upLoadSigleFile(String filePath, ImageUpLoadCallback imageUpLoadCallback, boolean errRetry) {
//
//        //开始
//        if (imageUpLoadCallback != null) {
//            imageUpLoadCallback.onStart();
//        }
//
//
//        if (TextUtils.isEmpty(filePath) || !new File(filePath).exists()) {
//            LogUtil.e("文件_" + filePath + "：文件不存在");
//            if (imageUpLoadCallback != null) {
//                imageUpLoadCallback.onFinish();
//            }
//            return;
//        }
//
//        File file = new File(filePath);
//        long fileTotalSize = file.length();
//
//        String tempFileName = "zt_" + System.currentTimeMillis() + "_" + RandomUntil.getNumLargeSmallLetter(5) + "_" + file.getName();
//
//
//        // 构造上传请求。
//
//        PutObjectRequest put = new PutObjectRequest(mBucket, tempFileName, file.getAbsolutePath());
//        put.setCRC64(OSSRequest.CRC64Config.YES);
//
//        // 异步上传时可以设置进度回调。
//        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
//            @Override
//            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
//                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
//                //计算当前大小
//                if (imageUpLoadCallback != null) {
//                    imageUpLoadCallback.onProgress(currentSize, fileTotalSize);
//                }
//            }
//        });
//
//
//        OSSAsyncTask task = ((MApplication) (MApplication.sAppContext)).getOss().asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
//            @Override
//            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
//                Log.d("PutObject", "UploadSuccess");
//                Log.d("ETag", result.getETag());
//                Log.d("RequestId", result.getRequestId());
//                if (imageUpLoadCallback != null) {
//                    ArrayList<String> imgUrl = new ArrayList<>();
//                    String url = mOss.presignPublicObjectURL(mBucket, tempFileName);
//                    imgUrl.add(url);
//                    imageUpLoadCallback.onFinish();
//                    imageUpLoadCallback.onSuccess(imgUrl);
//                }
//            }
//
//            @Override
//            public void onFailure(PutObjectRequest request, ClientException
//                    clientExcepion, ServiceException serviceException) {
//                // 请求异常。
//                if (clientExcepion != null) {
//                    // 本地异常，如网络异常等。
//                    clientExcepion.printStackTrace();
//                }
//                if (serviceException != null) {
//                    // 服务异常。
//                    Log.e("ErrorCode", serviceException.getErrorCode());
//                    Log.e("RequestId", serviceException.getRequestId());
//                    Log.e("HostId", serviceException.getHostId());
//                    Log.e("RawMessage", serviceException.getRawMessage());
//                }
//
//                if (imageUpLoadCallback != null) {
//                    imageUpLoadCallback.onFinish();
//                    imageUpLoadCallback.onError(clientExcepion, serviceException);
//                }
//            }
//        });
//
//        // task.cancel(); // 可以取消任务。
//        // task.waitUntilFinished(); // 等待任务完成。
//    }
//
//
//    /**
//     * 单文件可断点续传操作，在网速不好或者文件比较大的情况下可以考虑使用
//     *
//     * @param filePath               待上传文件的本地路径
//     * @param imageUpLoadCallback    上传回调
//     * @param errRetry               上传出错重试的剩余次数
//     */
//    public void resumableUploadBigFile(String filePath, ImageUpLoadCallback imageUpLoadCallback, boolean errRetry) {
//
//        //开始
//        if (imageUpLoadCallback != null) {
//            imageUpLoadCallback.onStart();
//        }
//
//
//        if (TextUtils.isEmpty(filePath) || !new File(filePath).exists()) {
//            LogUtil.e("文件_" + filePath + "：文件不存在");
//            if (imageUpLoadCallback != null) {
//                imageUpLoadCallback.onFinish();
//            }
//            return;
//        }
//
//        File file = new File(filePath);
//        long fileTotalSize = file.length();
//
//        String tempFileName = "zt_" + System.currentTimeMillis() + "_" + RandomUntil.getNumLargeSmallLetter(5) + "_" + file.getName();
//
//
//        // 创建断点上传请求
//        ResumableUploadRequest request = new ResumableUploadRequest(mBucket, tempFileName, file.getAbsolutePath());
//        request.setCRC64(OSSRequest.CRC64Config.YES);
//        // 设置上传过程回调
//        request.setProgressCallback(new OSSProgressCallback<ResumableUploadRequest>() {
//            @Override
//            public void onProgress(ResumableUploadRequest request, long currentSize, long totalSize) {
//                Log.d("resumableUpload", "currentSize: " + currentSize + " totalSize: " + totalSize);
//                //计算当前大小
//                if (imageUpLoadCallback != null) {
//                    imageUpLoadCallback.onProgress(currentSize, fileTotalSize);
//                }
//            }
//        });
//        // 异步调用断点上传
//        OSSAsyncTask resumableTask = mOss.asyncResumableUpload(request, new OSSCompletedCallback<ResumableUploadRequest, ResumableUploadResult>() {
//            @Override
//            public void onSuccess(ResumableUploadRequest request, ResumableUploadResult result) {
//                Log.d("resumableUpload", "success!");
//                Log.d("PutObject", "UploadSuccess");
//                Log.d("ETag", result.getETag());
//                Log.d("RequestId", result.getRequestId());
//                if (imageUpLoadCallback != null) {
//                    ArrayList<String> imgUrl = new ArrayList<>();
//                    String url = mOss.presignPublicObjectURL(mBucket, tempFileName);
//                    imgUrl.add(url);
//                    imageUpLoadCallback.onFinish();
//                    imageUpLoadCallback.onSuccess(imgUrl);
//                }
//            }
//
//            @Override
//            public void onFailure(ResumableUploadRequest request, ClientException clientExcepion, ServiceException serviceException) {
//                // 请求异常。
//                if (clientExcepion != null) {
//                    // 本地异常，如网络异常等。
//                    clientExcepion.printStackTrace();
//                }
//                if (serviceException != null) {
//                    // 服务异常。
//                    Log.e("ErrorCode", serviceException.getErrorCode());
//                    Log.e("RequestId", serviceException.getRequestId());
//                    Log.e("HostId", serviceException.getHostId());
//                    Log.e("RawMessage", serviceException.getRawMessage());
//                }
//
//                // 异常处理
//                if (imageUpLoadCallback != null) {
//                    imageUpLoadCallback.onFinish();
//                    imageUpLoadCallback.onError(clientExcepion, serviceException);
//                }
//            }
//        });
//
//        // resumableTask.waitUntilFinished(); // 可以等待直到任务完成
//
//    }
//
//
//    /**
//     * 多张图片一起上传服务器，并将上传后的图片路径按照对应的路径返回
//     *
//     * @param imgsList                  本地文件存储路径
//     * @param onErrorRetry              上传出错重试的剩余次数
//     * @param imageUpLoadCallback       上传回调
//     */
//    public void asyncImagesUpLoad(ArrayList<String> imgsList, boolean onErrorRetry, ImageUpLoadCallback imageUpLoadCallback) {
//        //开始
//        if (imageUpLoadCallback != null) {
//            imageUpLoadCallback.onStart();
//        }
//
//        ArrayList<UploadFile> uploadFiles = getEffectiveAllFiles(imgsList, onErrorRetry);
//
//        //没有可以上传的有效文件直接停止
//        if (uploadFiles.size() == 0) {
//            if (imageUpLoadCallback != null) {
//                imageUpLoadCallback.onFinish();
//            }
//            return;
//        }
//
//
//        long fileTotalSize = getEffectiveAllFilesSize(uploadFiles);
//
//
//        for (int i = 0; i < uploadFiles.size(); i++) {
//            OSSAsyncTask ossAsyncTask = asyncImageUpLoad(i, uploadFiles.get(i).filePath, true, new ImgProgressCallback() {
//                @Override
//                public void currentCount(int position, long currentCount) {
//                    uploadFiles.get(position).setFileCurrentSize(currentCount);
//                    LogUtil.e("文件大小" + currentCount);
//
//                    //计算当前大小
//                    if (imageUpLoadCallback != null) {
//                        long currentSize = 0;
//                        for (int i = 0; i < uploadFiles.size(); i++) {
//                            currentSize += uploadFiles.get(i).fileCurrentSize;
//                        }
//                        imageUpLoadCallback.onProgress(currentSize, fileTotalSize);
//                    }
//                }
//
//                @Override
//                public void onUpSuccess(int position, String fileUrl) {
//                    uploadFiles.get(position).setUpLoadSuccess(true);
//                    uploadFiles.get(position).setFileUrl(fileUrl);
//
//                    if (getSuccessFileCount(uploadFiles) == uploadFiles.size() && imageUpLoadCallback != null) {
//                        ArrayList<String> imgUrl = new ArrayList<>();
//                        for (int j = 0; j < uploadFiles.size(); j++) {
//                            String url = uploadFiles.get(j).fileUrl;
//                            if (!TextUtils.isEmpty(url)) {
//                                imgUrl.add(url);
//                            }
//                        }
//                        imageUpLoadCallback.onFinish();
//                        imageUpLoadCallback.onSuccess(imgUrl);
//                    }
//                }
//
//                @Override
//                public void onUpLaadError(int position, ClientException clientExcepion, ServiceException serviceException) {
//
//                    if (uploadFiles.get(position).getErrorRetryTimes() == 0 && imageUpLoadCallback != null) {
//                        imageUpLoadCallback.onFinish();
//                        imageUpLoadCallback.onError(clientExcepion, serviceException);
//                    }
//                    uploadFiles.get(position).onErrorRetry();
//                }
//            });
//        }
//    }
//
//
//    /**
//     * 将文件从一个存储空间（源存储空间）拷贝到同一地域的另一个存储空间（目标存储空间）中。
//     *
//     * @param srcBucketName     源文件服务器Bucket
//     * @param srcObjectKey      源文件名称
//     * @param destBucketName    目标文件服务器Bucket
//     * @param destObjectKey     目标文件名称
//     * @param bCallback         回调
//     */
//    public void copyObjectB2B(String srcBucketName, String srcObjectKey, String destBucketName, String destObjectKey, CopyB2BCallback bCallback) {
//        // 创建copy请求
//        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(srcBucketName, srcObjectKey,
//                destBucketName, destObjectKey);
//
//        // 可选设置copy文件元信息
//        // ObjectMetadata objectMetadata = new ObjectMetadata();
//        // objectMetadata.setContentType("application/octet-stream");
//        // copyObjectRequest.setNewObjectMetadata(objectMetadata);
//
//
//        //开始
//        if (bCallback != null) {
//            bCallback.onStart();
//        }
//
//
//        // 异步copy
//        OSSAsyncTask copyTask = mOss.asyncCopyObject(copyObjectRequest, new OSSCompletedCallback<CopyObjectRequest, CopyObjectResult>() {
//            @Override
//            public void onSuccess(CopyObjectRequest request, CopyObjectResult result) {
//                Log.d("copyObject", "copy success!");
//
//                if (bCallback != null) {
//                    bCallback.onFinish();
//                    bCallback.onSuccess(result);
//                }
//            }
//
//            @Override
//            public void onFailure(CopyObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
//                // 请求异常
//                if (clientExcepion != null) {
//                    // 本地异常如网络异常等
//                    clientExcepion.printStackTrace();
//                }
//                if (serviceException != null) {
//                    // 服务异常
//                    Log.e("ErrorCode", serviceException.getErrorCode());
//                    Log.e("RequestId", serviceException.getRequestId());
//                    Log.e("HostId", serviceException.getHostId());
//                    Log.e("RawMessage", serviceException.getRawMessage());
//                }
//
//                if (bCallback != null) {
//                    bCallback.onFinish();
//                    bCallback.onError(clientExcepion, serviceException);
//                }
//            }
//        });
//    }
//
//
//    /**
//     * 通过文件名删除指定地域对象存储服务器的对象，请注意不同地域相同文件名对象视为不同文件，无法同时删除
//     *
//     * @param objectKey
//     */
//    public void deleteObject(String objectKey, DeleteObjectCallback callback) {
//
//        //开始
//        if (callback != null) {
//            callback.onStart();
//        }
//
//        // 创建删除请求。
//        DeleteObjectRequest delete = new DeleteObjectRequest(mBucket, objectKey);
//        // 异步删除。
//        OSSAsyncTask deleteTask = mOss.asyncDeleteObject(delete, new OSSCompletedCallback<DeleteObjectRequest, DeleteObjectResult>() {
//            @Override
//            public void onSuccess(DeleteObjectRequest request, DeleteObjectResult result) {
//                Log.d("asyncCopyAndDelObject", "success!");
//                if (callback != null) {
//                    callback.onFinish();
//                    callback.onSuccess(result);
//                }
//            }
//
//            @Override
//            public void onFailure(DeleteObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
//                // 请求异常
//                if (clientExcepion != null) {
//                    // 本地异常如网络异常等
//                    clientExcepion.printStackTrace();
//                }
//                if (serviceException != null) {
//                    // 服务异常
//                    Log.e("ErrorCode", serviceException.getErrorCode());
//                    Log.e("RequestId", serviceException.getRequestId());
//                    Log.e("HostId", serviceException.getHostId());
//                    Log.e("RawMessage", serviceException.getRawMessage());
//                }
//
//                if (callback != null) {
//                    callback.onFinish();
//                    callback.onError(clientExcepion, serviceException);
//                }
//            }
//        });
//    }
//
//
//    /**
//     * 获取当前所有的上传成功的文件个数
//     *
//     * @param uploadFiles
//     * @return
//     */
//    private int getSuccessFileCount(ArrayList<UploadFile> uploadFiles) {
//        int count = 0;
//        for (int i = 0; i < uploadFiles.size(); i++) {
//            if (uploadFiles.get(i).isUpLoadSuccess()) {
//                count++;
//            }
//        }
//        return count;
//    }
//
//
//    /**
//     * 计算所有可上传文件的大小
//     *
//     * @param uploadFiles
//     */
//    private long getEffectiveAllFilesSize(ArrayList<UploadFile> uploadFiles) {
//        long fileTotalSize = 0;
//        for (int i = 0; i < uploadFiles.size(); i++) {
//            fileTotalSize += new File(uploadFiles.get(i).filePath).length();
//        }
//        return fileTotalSize;
//    }
//
//
//    /**
//     * 检查并获取所有可以上传的有效文件，文件不存在视为无效
//     *
//     * @param imgsList
//     * @param onErrorRetry
//     */
//    private ArrayList<UploadFile> getEffectiveAllFiles(ArrayList<String> imgsList, boolean onErrorRetry) {
//        ArrayList<UploadFile> uploadFiles = new ArrayList<>();
//
//
//        /**
//         * 检查路径存在
//         */
//        if (imgsList == null || imgsList.size() == 0) {
//            return uploadFiles;
//        }
//
//
//        /**
//         * 检查是否有可用文件
//         */
//        for (int i = 0; i < imgsList.size(); i++) {
//            String filePath = imgsList.get(i);
//            File file = new File(filePath);
//            if (!TextUtils.isEmpty(filePath) && file.exists()) {
//                uploadFiles.add(new UploadFile(i, filePath, 0L, null, false, onErrorRetry ? 1 : 0));
//            }
//        }
//
//        return uploadFiles;
//    }
//
//
//    /**
//     * 单张照片异步上传
//     *
//     * @param position
//     * @param filePath
//     * @param retry            单张照片上传失败会重试，这里设置重试的次数
//     * @param progressCallback
//     * @return
//     */
//    private OSSAsyncTask asyncImageUpLoad(int position, String filePath, boolean retry, final ImgProgressCallback progressCallback) {
//
//        if (TextUtils.isEmpty(filePath)) {
//            LogUtil.e("文件名_" + filePath + "不存在");
//            return null;
//        }
//        File file = new File(filePath);
//        if (!file.exists() || file.isDirectory()) {
//            LogUtil.e("文件_" + filePath + "不存在或是一个文件夹");
//            return null;
//        }
//
//
//        String tempFileName = "zt_" + System.currentTimeMillis() + "_" + RandomUntil.getNumLargeSmallLetter(5) + "_" + file.getName();
//
//
//        PutObjectRequest put = new PutObjectRequest(mBucket, tempFileName, file.getAbsolutePath());
//        put.setCRC64(OSSRequest.CRC64Config.YES);
//
//        /*if (mCallbackAddress != null) {
//            // 传入对应的上传回调参数，这里默认使用OSS提供的公共测试回调服务器地址
//            put.setCallbackParam(new HashMap<String, String>() {
//                {
//                    put("callbackUrl", mCallbackAddress);
//                    //callbackBody可以自定义传入的信息
//                    put("callbackBody", "filename=${object}");
//                }
//            });
//        }*/
//
//        // 异步上传时可以设置进度回调
//        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
//            @Override
//            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
//                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
//                int progress = (int) (100 * currentSize / totalSize);
//
//                if (progressCallback != null) {
//                    progressCallback.currentCount(position, currentSize);
//                }
//            }
//        });
//
//        return mOss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
//            @Override
//            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
//                Log.d("PutObject", "UploadSuccess");
//                Log.d("ETag", result.getETag());
//                Log.d("RequestId", result.getRequestId());
//                long upload_end = System.currentTimeMillis();
//                if (progressCallback != null) {
//                    progressCallback.onUpSuccess(position, mOss.presignPublicObjectURL(mBucket, tempFileName));
//                }
//            }
//
//            @Override
//            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
//                String info = "";
//                if (progressCallback != null) {
//                    progressCallback.onUpLaadError(position, clientExcepion, serviceException);
//                }
//                // 请求异常
//                if (clientExcepion != null) {
//                    // 本地异常如网络异常等
//                    clientExcepion.printStackTrace();
//                    info = clientExcepion.toString();
//                }
//                if (serviceException != null) {
//                    // 服务异常
//                    Log.e("ErrorCode", serviceException.getErrorCode());
//                    Log.e("RequestId", serviceException.getRequestId());
//                    Log.e("HostId", serviceException.getHostId());
//                    Log.e("RawMessage", serviceException.getRawMessage());
//                    info = serviceException.toString();
//                }
//            }
//        });
//    }
//
//    /**
//     * 单张照片异步上传
//     */
//    private OSSAsyncTask asyncImageUpLoad(String fileName, String filePath) {
//        String tempFileName = fileName;
//
//        if (TextUtils.isEmpty(filePath)) {
//            LogUtil.e("文件名_" + filePath + "不存在");
//            return null;
//        }
//        File file = new File(filePath);
//        if (!file.exists() || file.isDirectory()) {
//            LogUtil.e("文件_" + filePath + "不存在或是一个文件夹");
//            return null;
//        }
//
//        if (TextUtils.isEmpty(fileName)) {
//            tempFileName = "zc_" + System.currentTimeMillis() + "_" + file.getName();
//        }
//
//
//        PutObjectRequest put = new PutObjectRequest(mBucket, tempFileName, file.getAbsolutePath());
//        put.setCRC64(OSSRequest.CRC64Config.YES);
//        /*if (mCallbackAddress != null) {
//            // 传入对应的上传回调参数，这里默认使用OSS提供的公共测试回调服务器地址
//            put.setCallbackParam(new HashMap<String, String>() {
//                {
//                    put("callbackUrl", mCallbackAddress);
//                    //callbackBody可以自定义传入的信息
//                    put("callbackBody", "filename=${object}");
//                }
//            });
//        }*/
//
//        // 异步上传时可以设置进度回调
//        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
//            @Override
//            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
//                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
//                int progress = (int) (100 * currentSize / totalSize);
//            }
//        });
//
//        return mOss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
//            @Override
//            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
//                Log.d("PutObject", "UploadSuccess");
//                Log.d("ETag", result.getETag());
//                Log.d("RequestId", result.getRequestId());
//                long upload_end = System.currentTimeMillis();
//            }
//
//            @Override
//            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
//                String info = "";
//                // 请求异常
//                if (clientExcepion != null) {
//                    // 本地异常如网络异常等
//                    clientExcepion.printStackTrace();
//                    info = clientExcepion.toString();
//                }
//                if (serviceException != null) {
//                    // 服务异常
//                    Log.e("ErrorCode", serviceException.getErrorCode());
//                    Log.e("RequestId", serviceException.getRequestId());
//                    Log.e("HostId", serviceException.getHostId());
//                    Log.e("RawMessage", serviceException.getRawMessage());
//                    info = serviceException.toString();
//                }
//            }
//        });
//    }
//
//
//    public interface ImageUpLoadCallback {
//        /**
//         * 开始上传
//         */
//        void onStart();
//
//        /**
//         * 上传结束
//         */
//        void onFinish();
//
//        /**
//         * 上传成功
//         */
//        void onSuccess(ArrayList<String> fileUrlList);
//
//        /**
//         * 上传失败
//         */
//        void onError(ClientException clientExcepion, ServiceException serviceException);
//
//        /**
//         * progress
//         */
//        void onProgress(long currentSize, long totalSize);
//    }
//
//
//    interface ImgProgressCallback {
//        void currentCount(int position, long currentCount);
//
//        void onUpSuccess(int position, String fileUrl);
//
//        void onUpLaadError(int position, ClientException clientExcepion, ServiceException serviceException);
//
//    }
//
//
//    /**
//     * 待上传的文件
//     */
//    public static class UploadFile {
//        private int fileOrder;
//        private String filePath;
//        private long fileCurrentSize;
//        private String fileUrl;
//        private boolean upLoadSuccess;
//        private int errorRetryTimes;
//
//        public UploadFile(int fileOrder, String filePath, long fileCurrentSize, String fileUrl, boolean upLoadSuccess, int errorRetryTimes) {
//            this.fileOrder = fileOrder;
//            this.filePath = filePath;
//            this.fileCurrentSize = fileCurrentSize;
//            this.fileUrl = fileUrl;
//            this.upLoadSuccess = upLoadSuccess;
//            this.errorRetryTimes = errorRetryTimes;
//        }
//
//        public int getFileOrder() {
//            return fileOrder;
//        }
//
//        public void setFileOrder(int fileOrder) {
//            this.fileOrder = fileOrder;
//        }
//
//        public String getFilePath() {
//            return filePath;
//        }
//
//        public void setFilePath(String filePath) {
//            this.filePath = filePath;
//        }
//
//        public long getFileCurrentSize() {
//            return fileCurrentSize;
//        }
//
//        public void setFileCurrentSize(long fileCurrentSize) {
//            this.fileCurrentSize = fileCurrentSize;
//        }
//
//        public String getFileUrl() {
//            return fileUrl;
//        }
//
//        public void setFileUrl(String fileUrl) {
//            this.fileUrl = fileUrl;
//        }
//
//        public boolean isUpLoadSuccess() {
//            return upLoadSuccess;
//        }
//
//        public void setUpLoadSuccess(boolean upLoadSuccess) {
//            this.upLoadSuccess = upLoadSuccess;
//        }
//
//        public int getErrorRetryTimes() {
//            return errorRetryTimes;
//        }
//
//        public void setErrorRetryTimes(int errorRetryTimes) {
//            this.errorRetryTimes = errorRetryTimes;
//        }
//
//
//        public void onErrorRetry() {
//            errorRetryTimes--;
//        }
//    }
//
//
//    public interface CopyB2BCallback {
//        /**
//         *  开始拷贝
//         */
//        void onStart();
//        /**
//         *  结束拷贝
//         */
//        void onFinish();
//
//        /**
//         * 拷贝成功
//         * @param result
//         */
//        void onSuccess(CopyObjectResult result);
//
//        /**
//         * 拷贝失败
//         * @param clientExcepion
//         * @param serviceException
//         */
//        void onError(ClientException clientExcepion, ServiceException serviceException);
//    }
//
//    public interface DeleteObjectCallback {
//        void onStart();
//
//        void onFinish();
//
//        void onSuccess(DeleteObjectResult result);
//
//        void onError(ClientException clientExcepion, ServiceException serviceException);
//    }

}
