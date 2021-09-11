package com.zcgc.loveu.utils;

import android.content.Context;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.configure.PickerOptions;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;

import java.util.Calendar;
import java.util.List;

public class DialogUtils {
    public static TimePickerView showTimePickView(Context context, OnTimeSelectListener onTimeSelectListener
            , boolean[] types, String label1, String label2, String label3, String label4, String label5, String label6, Calendar calendar1, Calendar calendar2, String title, int count){
        return new TimePickerBuilder(context,onTimeSelectListener).
                setType(types).setLabel(label1,label2,label3,label4,label5,label6)
                .isCenterLabel(false).setRangDate(calendar1,calendar2).setTitleText(title).setItemVisibleCount(count).setLineSpacingMultiplier(3).build();

    }

    public static OptionsPickerView<String> showOptionsPickView(PickerOptions pickerOptions, String title, List<String> op){
        OptionsPickerView optionsPickerView=new OptionsPickerView(pickerOptions);
        optionsPickerView.setTitleText(title);
        optionsPickerView.setPicker(op);
        optionsPickerView.show();
        return optionsPickerView;
    }
    public static TimePickerView showTimePickView(PickerOptions pickerOptions, String title){
        TimePickerView optionsPickerView=new TimePickerView(pickerOptions);
        optionsPickerView.setTitleText(title);
        optionsPickerView.show();
        return optionsPickerView;
    }
}
