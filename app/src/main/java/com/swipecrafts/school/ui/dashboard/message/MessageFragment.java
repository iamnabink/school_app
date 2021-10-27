package com.swipecrafts.school.ui.dashboard.message;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.data.model.api.ApiResponse;
import com.swipecrafts.school.data.model.db.SchoolClass;
import com.swipecrafts.school.data.model.db.SchoolSection;
import com.swipecrafts.library.spinner.CustomSpinnerAdapter;
import com.swipecrafts.school.ui.base.BaseFragment;
import com.swipecrafts.school.utils.Constants;
import com.swipecrafts.school.utils.Utils;
import com.swipecrafts.school.viewmodel.ChatViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class MessageFragment extends BaseFragment {
    public static final String INBOX_TYPE = "Inbox";
    public static final String SENT_TYPE = "Sent";
    private static final String ARG_MESSAGE_TYPE = "ARG_MESSAGE_TYPE";
    public List<SchoolClass> classList;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private String mMessageTypeParam;
    private ChatViewModel chatUserViewModel;
    private RecyclerView mMessageRecyclerView;
    private MessageRecyclerAdapter mMessageRecyclerAdapter;
    private List<MessageModel> mMessageLists;
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeContainer;
    private LinearLayout errorLayout;
    private FloatingActionButton fabComposeMessage;
    private boolean isFirstRefresh = false;

    private int isClassSectionLoaded = -1;
    private int isInboxMessageLoaded = -1;
    private String errorMessage = "";

    // dialog
    private AlertDialog alertDialogAndroid;
    private View messageDialogView;

    public static MessageFragment newInstance(String mMessageTypeParam) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE_TYPE, mMessageTypeParam);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mMessageTypeParam = getArguments().getString(ARG_MESSAGE_TYPE);
        }

        ((SchoolApplication) getActivity().getApplication()).applicationComponent().inject(this);

        chatUserViewModel = ViewModelProviders.of(this, viewModelFactory).get(ChatViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        if (mMessageTypeParam.equalsIgnoreCase(INBOX_TYPE)) setHasOptionsMenu(true);

        findView(view);
        init();

        return view;
    }

    private void findView(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.messageToolBar);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.messageSwipeToRefreshLayout);
        errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);
        fabComposeMessage = (FloatingActionButton) view.findViewById(R.id.fabComposeMessage);

        if (mMessageTypeParam.equalsIgnoreCase(INBOX_TYPE))
            fabComposeMessage.setVisibility(View.VISIBLE);
        else fabComposeMessage.setVisibility(View.GONE);

        mMessageRecyclerView = (RecyclerView) view.findViewById(R.id.messageRecyclerView);
    }

    private void init() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Message: " + mMessageTypeParam);
        }

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(this::refreshMessageMessageList);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorPrimary,
                android.R.color.holo_red_light);

        // Set the adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration itemDecoration = new DividerItemDecoration(mMessageRecyclerView.getContext(), layoutManager.getOrientation());
        itemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.color.colorGrey));
        mMessageRecyclerView.addItemDecoration(itemDecoration);
        mMessageRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mMessageRecyclerView.setLayoutManager(layoutManager);

        if (mMessageLists == null) mMessageLists = new ArrayList<>();
        mMessageRecyclerAdapter = new MessageRecyclerAdapter(mMessageLists);
        mMessageRecyclerView.setAdapter(mMessageRecyclerAdapter);

        swipeContainer.post(() -> swipeContainer.setRefreshing(true));
        isFirstRefresh = true;
        refreshClassSection();
        refreshMessageMessageList();


        // Message Dialog Initialization
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        messageDialogView = layoutInflaterAndroid.inflate(R.layout.message_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
        alertDialogBuilderUserInput.setView(messageDialogView);
        alertDialogBuilderUserInput.setCancelable(false);
        alertDialogAndroid = alertDialogBuilderUserInput.create();

        fabComposeMessage.setOnClickListener(it -> showChooseUserDialog());
    }

    private void refreshMessageMessageList() {
        chatUserViewModel.getMessages(mMessageTypeParam).observe(this, response -> {
            if (response == null) return;

            if (response.isLoading()) {

            } else if (response.isSuccessful()) {
                Log.e("MessageS", "type" + mMessageTypeParam + " - " + response.data.size() + "");
                mMessageRecyclerAdapter.setMessageList(response.data);
                isInboxMessageLoaded = 1;
                if (!isFirstRefresh || isClassSectionLoaded == 1) {
                    mMessageRecyclerView.setVisibility(View.VISIBLE);
                    errorLayout.setVisibility(View.GONE);
                    isFirstRefresh = false;
                    swipeContainer.setRefreshing(false);
                } else if (isClassSectionLoaded == -1) {
                    swipeContainer.post(() -> swipeContainer.setRefreshing(false));
                    if (!isFirstRefresh) {
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    isFirstRefresh = false;
                    mMessageRecyclerView.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.VISIBLE);
                    TextView msg = errorLayout.findViewById(R.id.error_message);
                    msg.setText(errorMessage);
                }
            } else {
                Log.e("MessageE", "type " + mMessageTypeParam + " - " + response.message + " classSection" + isClassSectionLoaded);
                errorMessage = (response.status == 1001 ? Utils.isOnline(getContext()) ? Constants.UNKNOWN_ERROR_MESSAGE : Constants.NO_INTERNET_MESSAGE : response.message);
                isInboxMessageLoaded = 0;
                if (isClassSectionLoaded != -1) {
                    swipeContainer.post(() -> swipeContainer.setRefreshing(false));
                    if (!isFirstRefresh) {
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mMessageRecyclerView.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.VISIBLE);
                    TextView msg = errorLayout.findViewById(R.id.error_message);
                    msg.setText(errorMessage);
                    isFirstRefresh = false;
                }
            }
        });
    }

    private void refreshClassSection() {
        chatUserViewModel.getClassSections().observe(this, resource -> {
            if (resource == null) return;

            if (resource.isLoading()) {

            } else if (resource.isSuccessful()) {
                classList = resource.data;
                Log.e("ClassSectionS", resource.data.size() + "");
                isClassSectionLoaded = 1;
                if (isInboxMessageLoaded == 1) {
                    mMessageRecyclerView.setVisibility(View.VISIBLE);
                    errorLayout.setVisibility(View.GONE);
                    isFirstRefresh = false;
                    swipeContainer.setRefreshing(false);
                } else if (isInboxMessageLoaded != -1) {
                    swipeContainer.post(() -> swipeContainer.setRefreshing(false));
                    if (!isFirstRefresh) {
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    isFirstRefresh = false;
                    mMessageRecyclerView.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.VISIBLE);
                    TextView msg = errorLayout.findViewById(R.id.error_message);
                    msg.setText(errorMessage);
                }
            } else {
                Log.e("ClassSectionE", resource.message + " " + isInboxMessageLoaded);
                errorMessage = (resource.code() == 1001 ? Utils.isOnline(getContext()) ? Constants.UNKNOWN_ERROR_MESSAGE : Constants.NO_INTERNET_MESSAGE : resource.message);
                isClassSectionLoaded = 0;

                if (isInboxMessageLoaded != -1) {
                    swipeContainer.post(() -> swipeContainer.setRefreshing(false));
                    if (!isFirstRefresh) {
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mMessageRecyclerView.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.VISIBLE);
                    TextView msg = errorLayout.findViewById(R.id.error_message);
                    msg.setText(errorMessage);
                    isFirstRefresh = false;
                }
            }
        });
    }

    private void showChooseUserDialog() {
        FrameLayout userListFrameLyt = messageDialogView.findViewById(R.id.chooseStudentUserLyt);
        FrameLayout sendMessageLyt = messageDialogView.findViewById(R.id.sendMessageLyt);
        FrameLayout progressMessageLyt = messageDialogView.findViewById(R.id.progressFrame);
        progressMessageLyt.setVisibility(View.GONE);
        sendMessageLyt.setVisibility(View.GONE);
        userListFrameLyt.setVisibility(View.VISIBLE);


        Spinner classListSpinner = (Spinner) messageDialogView.findViewById(R.id.classListSpinner);
        Spinner sectionListSpinner = (Spinner) messageDialogView.findViewById(R.id.sectionListSpinner);
        LinearLayout sectionLayout = messageDialogView.findViewById(R.id.sectionLayout);
        LinearLayout spinnerLyt = messageDialogView.findViewById(R.id.chooseClassSectionLyt);

        Button btnCancel = (Button) messageDialogView.findViewById(R.id.cancelBtn);
        Button btnSend = (Button) messageDialogView.findViewById(R.id.sendMessageBtn);
        btnSend.setVisibility(View.GONE);

        TextView errorTV = (TextView) messageDialogView.findViewById(R.id.errorTextView);
        ListView studentListView = (ListView) messageDialogView.findViewById(R.id.usersListView);
        UserListAdapter userListAdapter = new UserListAdapter(getContext(), new ArrayList<>(), id -> {
            Log.e("UserClicked", id + "");
            changeToSendMessageView(id);
        });

        studentListView.setAdapter(userListAdapter);
        btnCancel.setOnClickListener((btn) -> alertDialogAndroid.dismiss());
        alertDialogAndroid.show();

        String userType = chatUserViewModel.getUserType();
        if (userType.equalsIgnoreCase("teacher")) {
            spinnerLyt.setVisibility(View.VISIBLE);
        } else {
            spinnerLyt.setVisibility(View.GONE);
            loadTeacherLists(userListFrameLyt, progressMessageLyt, studentListView, errorTV, userListAdapter);
            return;
        }

        CustomSpinnerAdapter<SchoolClass> classListAdapter = new CustomSpinnerAdapter<>(getContext(), R.layout.simple_spinner_item, new SchoolClass[]{new SchoolClass(-1, "none")});
        classListSpinner.setAdapter(classListAdapter);

        CustomSpinnerAdapter<SchoolSection> sectionListAdapter = new CustomSpinnerAdapter<>(getContext(), R.layout.simple_spinner_item, new SchoolSection[]{new SchoolSection(-1, "none")});
        sectionListSpinner.setAdapter(sectionListAdapter);

        if (classList == null) classList = new ArrayList<>();
        loadClassList(classListSpinner, classListAdapter, classList);

        classListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SchoolClass classResponse = classListAdapter.getItem(position);
                long classId = classResponse.getClassId();
                if (classId == -1) {
                    // do nothing
                    userListAdapter.updateStudentList(new ArrayList<>());
                    return;
                }

                LiveData<List<SchoolSection>> sectionData = chatUserViewModel.getSections(classId);
                loadSectionList(sectionListSpinner, sectionListAdapter, new ArrayList<>());
                sectionData.observe(MessageFragment.this, sections -> {
                    if (sections == null || sections.isEmpty()) {
                        // request for the students list
                        sectionLayout.setVisibility(View.GONE);
                        userListAdapter.updateStudentList(new ArrayList<>());
                        loadStudentLists(userListFrameLyt, progressMessageLyt, studentListView, errorTV, userListAdapter, classResponse.getClassId(), -1);
                        sectionData.removeObservers(MessageFragment.this);
                    } else {
                        sectionLayout.setVisibility(View.VISIBLE);
                        userListAdapter.updateStudentList(new ArrayList<>());
                        loadSectionList(sectionListSpinner, sectionListAdapter, sections);
                        sectionData.removeObservers(MessageFragment.this);
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sectionListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SchoolSection section = sectionListAdapter.getItem(position);
                if (section.getSectionId() == -1) {
                    // do nothing
                    userListAdapter.updateStudentList(new ArrayList<>());
                } else {
                    // request for the student lists
                    loadStudentLists(userListFrameLyt, progressMessageLyt, studentListView, errorTV, userListAdapter, section.getClassId(), section.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void changeToSendMessageView(long userId) {
        FrameLayout userListFrameLyt = messageDialogView.findViewById(R.id.chooseStudentUserLyt);
        FrameLayout sendMessageLyt = messageDialogView.findViewById(R.id.sendMessageLyt);
        FrameLayout progressMessageLyt = messageDialogView.findViewById(R.id.progressFrame);
        progressMessageLyt.setVisibility(View.GONE);
        sendMessageLyt.setVisibility(View.VISIBLE);
        userListFrameLyt.setVisibility(View.GONE);

        EditText edtMessage = messageDialogView.findViewById(R.id.edtMessage);

        edtMessage.setText("");
        Button btnSend = (Button) messageDialogView.findViewById(R.id.sendMessageBtn);
        btnSend.setVisibility(View.VISIBLE);

        btnSend.setOnClickListener((btn) -> {
            // send message!!
            String message = edtMessage.getText().toString();
            edtMessage.setError(null);
            if (TextUtils.isEmpty(message)) {
                edtMessage.setError("message can not be empty!!");
                return;
            }
            sendMessageLyt.setVisibility(View.GONE);
            progressMessageLyt.setVisibility(View.VISIBLE);
            LiveData<ApiResponse<String>> postMessage = chatUserViewModel.sendMessage(message, userId);

            postMessage.observe(this, response -> {
                if (response == null) return;

                if (response.isLoading()) {

                } else if (response.isSuccessful()) {
                    sendMessageLyt.setVisibility(View.VISIBLE);
                    progressMessageLyt.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Message successfully send!!", Toast.LENGTH_SHORT).show();
                    edtMessage.setText("");
                    postMessage.removeObservers(this);
                } else {
                    String errorMessage = (response.status == 1001 ? Utils.isOnline(getContext()) ? Constants.UNKNOWN_ERROR_MESSAGE : Constants.NO_INTERNET_MESSAGE : response.message);

                    sendMessageLyt.setVisibility(View.VISIBLE);
                    progressMessageLyt.setVisibility(View.GONE);
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    postMessage.removeObservers(this);
                }
            });
        });
    }

    private void loadClassList(Spinner spinner, CustomSpinnerAdapter<SchoolClass> adapter, List<SchoolClass> classes) {
        SchoolClass[] classArray = new SchoolClass[classes.size() + 1];
        classArray[0] = new SchoolClass(-1, "Choose");
        for (int i = 0; i < classes.size(); i++) {
            classArray[i + 1] = classes.get(i);
        }
        adapter.updateItems(classArray);
    }

    private void loadSectionList(Spinner spinner, CustomSpinnerAdapter<SchoolSection> adapter, List<SchoolSection> sectionList) {
        SchoolSection[] sectionArray = new SchoolSection[sectionList.size() + 1];
        sectionArray[0] = new SchoolSection(-1, "Choose");
        for (int i = 0; i < sectionList.size(); i++) {
            sectionArray[i + 1] = sectionList.get(i);
        }
        adapter.updateItems(sectionArray);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (mMessageTypeParam.equalsIgnoreCase(INBOX_TYPE))
            inflater.inflate(R.menu.message_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sentMessage:
                MessageFragment fragment = MessageFragment.newInstance(MessageFragment.SENT_TYPE);
                replaceFragment(R.id.main_container, fragment, MessageFragment.class.getSimpleName(), MessageFragment.SENT_TYPE);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadStudentLists(FrameLayout mainFrame, FrameLayout progressLyt, ListView userListView, TextView errorTV, UserListAdapter adapter, long classId, int sectionId) {
        mainFrame.setVisibility(View.GONE);
        userListView.setVisibility(View.VISIBLE);
        progressLyt.setVisibility(View.VISIBLE);

        chatUserViewModel.getUsersList(classId, sectionId).observe(this, response -> {
            if (response == null) return;

            if (response.isLoading()) {

            } else if (response.isSuccessful()) {
                Log.e("MessageUsers", response.data.size() + "");
                adapter.updateStudentList(response.data);
                mainFrame.setVisibility(View.VISIBLE);
                errorTV.setVisibility(View.GONE);
                progressLyt.setVisibility(View.GONE);
            } else {
                Log.e("MessageUsers", response.message);
                String message = (response.status == 1001 ? Utils.isOnline(getContext()) ? Constants.UNKNOWN_ERROR_MESSAGE : Constants.NO_INTERNET_MESSAGE : response.message);

                mainFrame.setVisibility(View.VISIBLE);
                progressLyt.setVisibility(View.GONE);
                userListView.setVisibility(View.GONE);
                errorTV.setVisibility(View.VISIBLE);
                errorTV.setText(message);
            }
        });
    }

    private void loadTeacherLists(FrameLayout mainFrame, FrameLayout progressLyt, ListView userListView, TextView errorTV, UserListAdapter adapter) {
        mainFrame.setVisibility(View.GONE);
        userListView.setVisibility(View.VISIBLE);
        progressLyt.setVisibility(View.VISIBLE);

        chatUserViewModel.getTeacherList().observe(this, response -> {
            if (response == null) return;

            if (response.isLoading()) {
            } else if (response.isSuccessful()) {
                Log.e("MessageUsers", response.data.size() + "");
                adapter.updateStudentList(response.data);
                mainFrame.setVisibility(View.VISIBLE);
                errorTV.setVisibility(View.GONE);
                progressLyt.setVisibility(View.GONE);
            } else {
                Log.e("MessageUsers", response.message);
                String errorMessage = (response.status == 1001 ? Utils.isOnline(getContext()) ? Constants.UNKNOWN_ERROR_MESSAGE : Constants.NO_INTERNET_MESSAGE : response.message);

                mainFrame.setVisibility(View.VISIBLE);
                progressLyt.setVisibility(View.GONE);
                userListView.setVisibility(View.GONE);
                errorTV.setVisibility(View.VISIBLE);
                errorTV.setText(errorMessage);
            }
        });
    }
}
