package com.boredream.baseapplication.base;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import com.boredream.baseapplication.BR;
import com.boredream.baseapplication.ViewModelFactory;

abstract public class BaseActivity<VM extends BaseViewModel, BD extends ViewDataBinding> extends AppCompatActivity {

    protected VM viewModel;
    protected BD dataBinding;

    abstract protected int getLayoutId();

    abstract protected Class<VM> getViewModelClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        ViewModelFactory factory = ViewModelFactory.getInstance();
        viewModel = new ViewModelProvider(this, factory).get(getViewModelClass());
        dataBinding.setLifecycleOwner(this);
        dataBinding.setVariable(BR.viewModel, viewModel);

        setupToast();
    }

    private void setupToast() {
        viewModel.getToastEvent().observe(this, msg -> Toast.makeText(this, msg, Toast.LENGTH_SHORT).show());
    }

}
