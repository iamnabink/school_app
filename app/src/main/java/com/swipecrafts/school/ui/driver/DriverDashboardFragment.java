package com.swipecrafts.school.ui.driver;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.data.manager.Status;
import com.swipecrafts.library.preetydialog.CustomDialog;
import com.swipecrafts.school.ui.MainActivity;
import com.swipecrafts.school.ui.base.BaseFragment;
import com.swipecrafts.school.ui.driver.bus.AssociatedBusFragment;
import com.swipecrafts.school.ui.driver.live.BusLocationFragment;
import com.swipecrafts.school.utils.Constants;
import com.swipecrafts.school.utils.Utils;
import com.swipecrafts.school.viewmodel.UserViewModel;

import javax.inject.Inject;


public class DriverDashboardFragment extends BaseFragment {
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private UserViewModel userViewModel;

    private View associatedBus, goToLiveNavigation, logout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((SchoolApplication) getActivity().getApplication())
                .applicationComponent()
                .inject(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_driver_dashboard, container, false);

        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);

        associatedBus = view.findViewById(R.id.associatedBusesContainer);
        goToLiveNavigation = view.findViewById(R.id.goToLiveBusContainer);
        logout = view.findViewById(R.id.logoutContainer);

        associatedBus.setOnClickListener(v -> getFragmentManager()
                .beginTransaction()
                .replace(R.id.driver_fragment_container, new AssociatedBusFragment())
                .addToBackStack(AssociatedBusFragment.class.getSimpleName())
                .commit());

        goToLiveNavigation.setOnClickListener(v -> getFragmentManager()
                .beginTransaction()
                .replace(R.id.driver_fragment_container, new BusLocationFragment())
                .addToBackStack(BusLocationFragment.class.getSimpleName())
                .commit());

        logout.setOnClickListener(v -> {
            CustomDialog dialog = showProgressDialog(null, "logout...");
            userViewModel.logOutUser().observe(this, response -> {
                if (response == null) return;

                if (response.status == Status.SUCCESS) {
                    showSuccessDialog(
                            dialog,
                            getString(R.string.logout_dialog_title),
                            getString(R.string.logout_message),
                            getString(R.string.dialog_ok),
                            customDialog -> {
                                String stop = "stop";
                                getActivity().sendBroadcast(new Intent(stop));
                                startActivity(new Intent(getContext(), MainActivity.class));
                            });
                } else if (response.status == Status.ERROR) {
                    String errorMessage = (response.code() == 1001 ? Utils.isOnline(getContext()) ? Constants.UNKNOWN_ERROR_MESSAGE : Constants.NO_INTERNET_MESSAGE : response.message);
                    showErrorDialog(dialog, getString(R.string.error_dialog_title), errorMessage, getString(R.string.dialog_ok), null);
                }
            });
        });


        return view;
    }

}
