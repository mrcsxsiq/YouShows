package org.michaelbel.ui.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.michaelbel.bottomsheet.BottomSheet;
import org.michaelbel.seriespicker.Browser;
import org.michaelbel.seriespicker.LayoutHelper;
import org.michaelbel.seriespicker.R;
import org.michaelbel.seriespicker.Theme;
import org.michaelbel.ui.MainActivity;
import org.michaelbel.ui.adapter.Holder;
import org.michaelbel.ui.cell.TextDetailCell;
import org.michaelbel.ui.view.RecyclerListView;
import org.michaelbel.util.ScreenUtils;

@SuppressWarnings("all")
public class LibsFragment extends Fragment {

    private final String bottomSheetUrl = "https://github.com/michaelbel/BottomSheet";
    private final String retrofitUrl = "https://square.github.io/retrofit";
    private final String rxJavaUrl = "https://github.com/ReactiveX/RxJava";
    private final String rxAndroidUrl = "https://github.com/ReactiveX/RxAndroid";
    private final String glideUrl = "https://bumptech.github.io/glide/";

    private int rowCount;
    private int bottomSheetRow;
    private int retrofitRow;
    private int rxJavaRow;
    private int rxAndroidRow;
    private int glideRow;

    private String url;
    private ListAdapter adapter;
    private MainActivity activity;
    private LinearLayoutManager layoutManager;

    private RecyclerListView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        FrameLayout fragmentView = new FrameLayout(activity);
        fragmentView.setBackgroundColor(ContextCompat.getColor(activity, Theme.backgroundColor()));

        activity.toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        activity.toolbar.setNavigationOnClickListener(view -> activity.finishFragment());
        activity.toolbarTextView.setText(R.string.OpenSourceLibs);

        rowCount = 0;
        bottomSheetRow = rowCount++;
        retrofitRow = rowCount++;
        rxJavaRow = rowCount++;
        rxAndroidRow = rowCount++;
        glideRow = rowCount++;

        adapter = new ListAdapter();
        layoutManager = new LinearLayoutManager(activity);

        recyclerView = new RecyclerListView(activity);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutParams(LayoutHelper.makeFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        recyclerView.setOnItemClickListener((view1, position) -> {
            String url = null;

            if (position == bottomSheetRow) {
                url = bottomSheetUrl;
            } else if (position == retrofitRow) {
                url = retrofitUrl;
            } else if (position == rxJavaRow) {
                url = rxJavaUrl;
            } else if (position == rxAndroidRow) {
                url = rxAndroidUrl;
            } else if (position == glideRow) {
                url = glideUrl;
            }

            if (url != null) {
                Browser.openUrl(url);
            }
        });
        recyclerView.setOnItemLongClickListener((view, position) -> {
            if (position == bottomSheetRow) {
                url = bottomSheetUrl;
            } else if (position == retrofitRow) {
                url = retrofitUrl;
            } else if (position == rxJavaRow) {
                url = rxJavaUrl;
            } else if (position == rxAndroidRow) {
                url = rxAndroidUrl;
            } else if (position == glideRow) {
                url = glideUrl;
            }

            BottomSheet.Builder builder = new BottomSheet.Builder(activity);
            builder.setTitle(url);
            builder.setDarkTheme(!Theme.getTheme());
            builder.setCellHeight(ScreenUtils.dp(52));
            builder.setItems(new int[] { R.string.Open, R.string.CopyLink }, (dialogInterface, i) -> {
                if (i == 0) {
                    Browser.openUrl(url);
                } else if (i == 1) {
                    ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Link", url);
                    if (clipboard != null) {
                        clipboard.setPrimaryClip(clip);
                    }

                    Toast.makeText(activity, R.string.LinkCopied, Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();
            return true;
        });
        fragmentView.addView(recyclerView);
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Parcelable state = layoutManager.onSaveInstanceState();
        layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.onRestoreInstanceState(state);
    }

    public class ListAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
            View cell = null;

            if (type == 0) {
                cell = new TextDetailCell(activity);
            }

            return new Holder(cell);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            int type = getItemViewType(position);

            if (type == 0) {
                TextDetailCell cell = (TextDetailCell) holder.itemView;
                cell.changeLayoutParams();

                if (position == bottomSheetRow) {
                    cell.setText("BottomSheet");
                    cell.setValue("Apache License v2.0");
                    cell.setDivider(true);
                } else if (position == retrofitRow) {
                    cell.setText("Retrofit");
                    cell.setValue("Apache License v2.0");
                    cell.setDivider(true);
                } else if (position == rxJavaRow) {
                    cell.setText("RxJava");
                    cell.setValue("Apache License v2.0");
                    cell.setDivider(true);
                } else if (position == rxAndroidRow) {
                    cell.setText("RxAndroid");
                    cell.setValue("Apache License v2.0");
                    cell.setDivider(true);
                } else if (position == glideRow) {
                    cell.setText("Glide");
                    cell.setValue("BSD, MIT and Apache License v2.0");
                }
            }
        }

        @Override
        public int getItemCount() {
            return rowCount;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == bottomSheetRow || position == retrofitRow || position == rxJavaRow ||
                    position == rxAndroidRow || position == glideRow) {
                return 0;
            } else {
                return 1;
            }
        }
    }
}