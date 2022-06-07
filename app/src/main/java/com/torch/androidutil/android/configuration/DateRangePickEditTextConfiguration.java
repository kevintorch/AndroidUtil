package com.torch.androidutil.android.configuration;

import android.view.View;
import android.view.ViewParent;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.core.util.Pair;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.torch.androidutil.android.ResourcesUtil;
import com.torch.androidutil.interfaces.EditTextConfiguration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DateRangePickEditTextConfiguration implements EditTextConfiguration {

    private final MaterialDatePicker.Builder<Pair<Long, Long>> dateRangePicker = MaterialDatePicker.Builder.dateRangePicker();
    private MaterialDatePicker<Pair<Long, Long>> build;
    private FragmentActivity activity;

    private final View.OnClickListener showPickerListener = v -> {
        build.show(activity.getSupportFragmentManager(), build.toString());
    };

    private DateRangePickEditTextConfiguration(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public void configure(EditText editText) {
        if (editText instanceof AutoCompleteTextView) {
            AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) editText;
            ViewParent parent = autoCompleteTextView.getParent();
            if (parent instanceof TextInputLayout) {
                ((TextInputLayout) parent).setEndIconOnClickListener(showPickerListener);
            }
            autoCompleteTextView.setOnClickListener(showPickerListener);
        }
    }

    private void apply(Builder builder) {
        long today = MaterialDatePicker.todayInUtcMilliseconds();
        Pair<Long, Long> selection = builder.selection == null ? Pair.create(today, today) : builder.selection;

//        dateRangePicker.setTheme(ResourcesUtil.getResource(activity, R.attr.materialCalendarFullscreenTheme));
        dateRangePicker.setSelection(selection);
        dateRangePicker.setCalendarConstraints(new CalendarConstraints.Builder()
                .setOpenAt(builder.openAt <= 0 ? selection.first : builder.openAt)
                .build());

        build = dateRangePicker.build();
        build.setCancelable(false);

        build.addOnPositiveButtonClickListener(
                new MultipleOnDateRangeChangeListener(builder.onDateRangeChangeListeners));
    }

    public interface OnDateRangeChangeListener {
        void onDateRangeChanged(Pair<Long, Long> dateRange);
    }

    private static class MultipleOnDateRangeChangeListener
            implements MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>> {
        private final Collection<OnDateRangeChangeListener> onDateRangeChangeListeners;

        public MultipleOnDateRangeChangeListener(Collection<OnDateRangeChangeListener> onDateRangeChangeListeners) {
            this.onDateRangeChangeListeners = onDateRangeChangeListeners;
        }

        @Override
        public void onPositiveButtonClick(Pair<Long, Long> selection) {
            for (OnDateRangeChangeListener listener : onDateRangeChangeListeners) {
                listener.onDateRangeChanged(selection);
            }
        }
    }

    public static class Builder {
        private final List<OnDateRangeChangeListener> onDateRangeChangeListeners = new ArrayList<>();
        private final FragmentActivity fragmentActivity;
        private Pair<Long, Long> selection;
        private long maxDate;
        private long minDate;
        private long openAt;
        private CalendarConstraints.DateValidator validator;

        public Builder(FragmentActivity fragmentActivity) {
            this.fragmentActivity = fragmentActivity;
        }

        public Builder addOnDateRangeChangeListener(OnDateRangeChangeListener rangeChangeListener) {
            if (rangeChangeListener != null)
                onDateRangeChangeListeners.add(rangeChangeListener);
            return this;
        }

        public Builder setSelection(Pair<Long, Long> selection) {
            this.selection = selection;
            return this;
        }

        /**
         * A UTC timeInMilliseconds contained within the latest month the calendar will page to.
         * Defaults December, 2100.
         *
         * <p>If you have access to java.time in Java 8, you can obtain a long using {@code
         * java.time.ZonedDateTime}.
         *
         * <pre>{@code
         * LocalDateTime local = LocalDateTime.of(year, month, 1, 0, 0);
         * local.atZone(ZoneId.ofOffset("UTC", ZoneOffset.UTC)).toInstant().toEpochMilli();
         * }</pre>
         *
         * <p>If you don't have access to java.time in Java 8, you can obtain this value using a {@code
         * java.util.Calendar} instance from the UTC timezone.
         *
         * <pre>{@code
         * Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
         * c.set(year, month, 1);
         * c.getTimeInMillis();
         * }</pre>
         */
        public Builder setMaxDate(Long maxDate) {
            this.maxDate = maxDate;
            return this;
        }


        /**
         * A UTC timeInMilliseconds contained within the earliest month the calendar will page to.
         * Defaults January, 1900.
         *
         * <p>If you have access to java.time in Java 8, you can obtain a long using {@code
         * java.time.ZonedDateTime}.
         *
         * <pre>{@code
         * LocalDateTime local = LocalDateTime.of(year, month, 1, 0, 0);
         * local.atZone(ZoneId.ofOffset("UTC", ZoneOffset.UTC)).toInstant().toEpochMilli();
         * }</pre>
         *
         * <p>If you don't have access to java.time in Java 8, you can obtain this value using a {@code
         * java.util.Calendar} instance from the UTC timezone.
         *
         * <pre>{@code
         * Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
         * c.set(year, month, 1);
         * c.getTimeInMillis();
         * }</pre>
         */
        public Builder setMinDate(Long minDate) {
            this.minDate = minDate;
            return this;
        }

        public Builder setOpenAt(long openAt) {
            this.openAt = openAt;
            return this;
        }

        public Builder setValidator(CalendarConstraints.DateValidator validator) {
            this.validator = validator;
            return this;
        }

        public DateRangePickEditTextConfiguration build() {
            DateRangePickEditTextConfiguration picker = new DateRangePickEditTextConfiguration(fragmentActivity);
            picker.apply(this);
            return picker;
        }

    }
}
