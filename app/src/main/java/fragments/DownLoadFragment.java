package fragments;


import java.util.ArrayList;
import java.util.List;

import org.wlf.filedownloader.DownloadFileInfo;
import org.wlf.filedownloader.DownloadStatusConfiguration;
import org.wlf.filedownloader.FileDownloader;
import org.wlf.filedownloader.listener.OnDeleteDownloadFileListener;
import org.wlf.filedownloader.listener.OnDeleteDownloadFilesListener;
import org.wlf.filedownloader.listener.OnDetectBigUrlFileListener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.anke.vedio.R;

import adapter.DownloadFileListAdapter;
import model.FileInfo;

/**
 * A simple {@link Fragment} subclass.
 */
public class DownLoadFragment extends Fragment implements DownloadFileListAdapter.OnItemSelectListener {
    private static final String SDCARD = Environment.getExternalStorageDirectory().getPath();
    private ArrayList<FileInfo> mDate=new ArrayList<>();
    private DownloadFileListAdapter mDownloadFileListAdapter;
    private Toast mToast;
    public static DownLoadFragment instance;
    public DownLoadFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.fragment_download, null);
        initview(layout);
    	CreatDownloadFile("http://pan.abn.cc/weiyun/down.php?u=9e850eb72732cff899434deff879afac.undefined.mp4","功夫熊猫.mp4");
        return layout;
    }
    public static DownLoadFragment getInstance(){
        if(instance==null){
            instance=new DownLoadFragment();
        }
        return instance;
    }
    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent();
        intent.putExtra("TiTle", "下载");
        intent.setAction("TiTle");   //
        getActivity().sendBroadcast(intent);   //发送广播
    }
    @Override
    public void onResume() {
        super.onResume();
//        ReGistBrocast();
        if (mDownloadFileListAdapter != null) {
            mDownloadFileListAdapter.updateShow();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
//        FileDownloader.pauseAll();
        FileDownloader.unregisterDownloadStatusListener(mDownloadFileListAdapter);
    }
    private void initview(View layout) {
        ListView lvDownloadFileList = (ListView) layout.findViewById(R.id.lvDownloadFileList);
        mDownloadFileListAdapter = new DownloadFileListAdapter(getActivity());
        lvDownloadFileList.setAdapter(mDownloadFileListAdapter);
        mDownloadFileListAdapter.setOnItemSelectListener(this);
        boolean isDownloadStatusConfigurationTest = false;// TEST
        if (!isDownloadStatusConfigurationTest) {
            // register to listen all
            FileDownloader.registerDownloadStatusListener(mDownloadFileListAdapter);
        } else {
            // register to only listen special url
            DownloadStatusConfiguration.Builder builder = new DownloadStatusConfiguration.Builder();
            builder.addListenUrl("http://182.254.149.157/ftp/image/shop/product/Kids Addition & Subtraction 1.0.apk");
            FileDownloader.registerDownloadStatusListener(mDownloadFileListAdapter, builder.build());
        }
    }
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
    @Override
    public void onSelected(final List<DownloadFileInfo> selectDownloadFileInfos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.main__confirm_whether_delete_save_file));
        builder.setNegativeButton(getString(R.string.main__confirm_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteDownloadFiles(false, selectDownloadFileInfos);
            }
        });
        builder.setPositiveButton(getString(R.string.main__confirm_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                deleteDownloadFiles(true, selectDownloadFileInfos);
            }
        });
        builder.show();
    }
    @Override
    public void onNoneSelect() {

    }
    private void showToast(CharSequence text) {
        if (mToast == null) {
            mToast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.cancel();
            mToast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    private void updateAdapter() {
        if (mDownloadFileListAdapter == null) {
            return;
        }
        mDownloadFileListAdapter.updateShow();
    }

    /**
     * 执行下载
     */
    public void CreatDownloadFile(String url,final String FileName){
        FileDownloader.detect(url, new OnDetectBigUrlFileListener() {
            // ----------------------detect url file callback----------------------
            @Override
            public void onDetectNewDownloadFile(final String url, String fileName, final String saveDir, long
                    fileSize) {
                FileDownloader.createAndStart(url, SDCARD, FileName);
            }
            @Override
            public void onDetectUrlFileExist(String url) {
                showToast(getString(R.string.main__continue_download) + url);
                Log.e("wlf", "探测文件，继续下载：" + url);
                // continue download
                FileDownloader.start(url);
            }
            @Override
            public void onDetectUrlFileFailed(String url, DetectBigUrlFileFailReason failReason) {
                String msg = null;
                if (failReason != null) {
                    msg = failReason.getMessage();
                    if (TextUtils.isEmpty(msg)) {
                        Throwable t = failReason.getCause();
                        if (t != null) {
                            msg = t.getLocalizedMessage();
                        }
                    }
                }
                showToast(getString(R.string.main__detect_file_error) + msg + "," + url);
                Log.e("wlf", "出错回调，探测文件出错：" + msg + "," + url);
            }
        });
    }

    /**
     * 删除文件
     * @param deleteDownloadedFile
     * @param selectDownloadFileInfos
     */
    private void deleteDownloadFiles(boolean deleteDownloadedFile, List<DownloadFileInfo>
            selectDownloadFileInfos) {

        List<String> urls = new ArrayList<String>();

        for (DownloadFileInfo downloadFileInfo : selectDownloadFileInfos) {
            if (downloadFileInfo == null) {
                continue;
            }
            urls.add(downloadFileInfo.getUrl());
        }

        // single delete
        if (urls.size() == 1) {
            FileDownloader.delete(urls.get(0), deleteDownloadedFile, new OnDeleteDownloadFileListener() {
                @Override
                public void onDeleteDownloadFileSuccess(DownloadFileInfo downloadFileDeleted) {
                    showToast(getString(R.string.main__delete_succeed));
                    updateAdapter();

                    Log.e("wlf", "onDeleteDownloadFileSuccess 成功回调，单个删除" + downloadFileDeleted.getFileName()
                            + "成功");
                }

                @Override
                public void onDeleteDownloadFilePrepared(DownloadFileInfo downloadFileNeedDelete) {
                    if (downloadFileNeedDelete != null) {
                        showToast(getString(R.string.main__deleting) + downloadFileNeedDelete.getFileName());
                    }
                }

                @Override
                public void onDeleteDownloadFileFailed(DownloadFileInfo downloadFileInfo,
                                                       DeleteDownloadFileFailReason failReason) {
                    showToast(getString(R.string.main__delete) + downloadFileInfo.getFileName() + getString(R
                            .string.main__failed));

                    Log.e("wlf", "onDeleteDownloadFileFailed 出错回调，单个删除" + downloadFileInfo.getFileName() +
                            "失败");
                }
            });
        }
        // multi delete
        else {
            Log.e("wlf_deletes", "点击开始批量删除");
            FileDownloader.delete(urls, deleteDownloadedFile, new OnDeleteDownloadFilesListener() {

                @Override
                public void onDeletingDownloadFiles(List<DownloadFileInfo> downloadFilesNeedDelete,
                                                    List<DownloadFileInfo> downloadFilesDeleted,
                                                    List<DownloadFileInfo> downloadFilesSkip,
                                                    DownloadFileInfo downloadFileDeleting) {
                    if (downloadFileDeleting != null) {
                        showToast(getString(R.string.main__deleting) + downloadFileDeleting.getFileName() +
                                getString(R.string.main__progress) + (downloadFilesDeleted.size() +
                                downloadFilesSkip.size()) + getString(R.string.main__failed2) +
                                downloadFilesSkip.size() + getString(R.string
                                .main__skip_and_total_delete_division) +
                                downloadFilesNeedDelete.size());
                    }
                    updateAdapter();
                }

                @Override
                public void onDeleteDownloadFilesPrepared(List<DownloadFileInfo> downloadFilesNeedDelete) {
                    showToast(getString(R.string.main__need_delete) + downloadFilesNeedDelete.size());
                }
                @Override
                public void onDeleteDownloadFilesCompleted(List<DownloadFileInfo> downloadFilesNeedDelete,
                                                           List<DownloadFileInfo> downloadFilesDeleted) {

                    String text = getString(R.string.main__delete_finish) + downloadFilesDeleted.size() +
                            getString(R.string.main__failed3) + (downloadFilesNeedDelete.size() -
                            downloadFilesDeleted.size());
                    showToast(text);
                    updateAdapter();
                    Log.e("wlf", "onDeleteDownloadFilesCompleted 完成回调，" + text);
                }
            });
        }
    }

}
