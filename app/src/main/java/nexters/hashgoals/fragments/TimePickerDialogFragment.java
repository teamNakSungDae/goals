package nexters.hashgoals.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;
import java.util.Calendar;
import nexters.hashgoals.R;

/**
 * Created by clsan on 21/04/2017.
 */
public class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState){
    final Calendar calendar = Calendar.getInstance();
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);

    TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
        R.style.dialog,
        this,
        hour,
        minute,
        false);
    timePickerDialog.setTitle("");

    return timePickerDialog;
  }

  @Override
  public void onTimeSet(TimePicker view, int hourOfDay, int minute){

  }
}
