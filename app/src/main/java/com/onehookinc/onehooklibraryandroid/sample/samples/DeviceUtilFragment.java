package com.onehookinc.onehooklibraryandroid.sample.samples;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onehook.util.device.DeviceInfoUtil;
import com.onehookinc.onehooklibraryandroid.R;
import com.onehookinc.onehooklibraryandroid.sample.common.BaseFragment;

public class DeviceUtilFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_device_util, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DeviceInfoUtil.init(getContext());

        final TextView resolutionTextView = view.findViewById(R.id.fragment_device_util_resolution);
        resolutionTextView.setText(String.format("%d x %d",
                DeviceInfoUtil.getDisplayPoint().x,
                DeviceInfoUtil.getDisplayPoint().y));

        final TextView deviceNameTextView = view.findViewById(R.id.fragment_device_util_name);
        deviceNameTextView.setText(DeviceInfoUtil.getDeviceName());

        final TextView deviceManufacturerTextView = view.findViewById(R.id.fragment_device_util_manufacturer);
        deviceManufacturerTextView.setText(DeviceInfoUtil.getDeviceManufacturer());

        final TextView devicePlatformTextView = view.findViewById(R.id.fragment_device_util_platform_version);
        devicePlatformTextView.setText(DeviceInfoUtil.getDevicePlatformVersion());

    }

    @Override
    public void onToolbarReady(Toolbar toolbar) {
        super.onToolbarReady(toolbar);
        getBaseAcivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getBaseAcivity().getSupportActionBar().setTitle("Device Utility");
    }
}
