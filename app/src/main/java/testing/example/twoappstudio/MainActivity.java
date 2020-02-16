package testing.example.twoappstudio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CustomDialog.CustomDialogListener {

   List<info> infoList;
   ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        infoList = new ArrayList<>();

        infoList.add(new info(R.drawable.cna, "Channel New Asia, SG News", "https://www.channelnewsasia.com/"));
        infoList.add(new info(R.drawable.yahoo4, "Yahoo", "https://sg.yahoo.com/"));
        infoList.add(new info(R.drawable.g, "Google", "https://www.google.com"));
        infoList.add(new info(R.drawable.linkedin, "LinkedIn", "https://www.linkedIn.com"));

        listView =findViewById(R.id.listView);
        final MyAdapter adapter = new MyAdapter(this, R.layout.row, infoList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                openDialog();


//                if(position == 0){
//                    Toast.makeText(MainActivity.this, "Facebook Description", Toast.LENGTH_SHORT).show();
//                }
//                if(position == 1){
//                    Toast.makeText(MainActivity.this, "Whatsapp Description", Toast.LENGTH_SHORT).show();
//                }
//                if(position == 2){
//                    Toast.makeText(MainActivity.this, "Twitter Description", Toast.LENGTH_SHORT).show();
//                }
//                if(position == 3){
//                    Toast.makeText(MainActivity.this, "Instagram Description", Toast.LENGTH_SHORT).show();
//                }

            }
        });


        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                final int checkedCount = listView.getCheckedItemCount();
                mode.setTitle(checkedCount + "Selected");
                adapter.toggleSelection(position);

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.main_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

                switch (item.getItemId()){
                    case R.id.delete:

                        SparseBooleanArray selected = adapter.getSelectedID();

                        for(int i=(selected.size()-1); i>=0; i--){
                            if(selected.valueAt(i)){
                                info selectedItem = adapter.getItem(selected.keyAt(i));

                                adapter.remove(selectedItem);
                            }
                        }
                        //CLOSE
                        mode.finish();
                        return true;

                    default:
                        return  false;
                }

            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                adapter.removeSelection();
            }
        });
    }


    public void openDialog(){
        CustomDialog customDialog = new CustomDialog();
        customDialog.show(getSupportFragmentManager(), "custom dialog");
    }

    @Override
    public void applyText(String link) {


    }

    public class MyAdapter extends ArrayAdapter<info>{

        Context context;
        int resource;
        List<info> infoList;
        private SparseBooleanArray sparseBooleanArray;

        public MyAdapter(Context c, int resource, List<info> infoList){
            super(c, resource, infoList);

           sparseBooleanArray = new SparseBooleanArray();
           this.context = c;
           this.resource = resource;
           this.infoList = infoList;
        }

//        Context context;
//        String rTitle[];
//        String rDescription[];
//        int rImg[];

//        public MyAdapter(Context c, String title[], String description[], int img[]){
//            //super(c, R.layout.activity_main, R.id.textView1, title);
//            super(c, R.layout.row, R.id.textView1, title);
//
//
//            this.context = c;
//            this.rTitle = title;
//            this.rDescription = description;
//            this.rImg = img;
//
//        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = inflater.inflate(R.layout.row, parent, false);

            ImageView imageView = row.findViewById(R.id.image);
            TextView myTitle = row.findViewById(R.id.textView1);
            TextView myDescription = row.findViewById(R.id.textView2);

            info i = infoList.get(position);

            imageView.setImageResource(i.getImage());
            myTitle.setText(i.getTitle());
            myDescription.setText(i.getDescription());


            return row;
        }

        public void remove(info object){
            infoList.remove(object);
            notifyDataSetChanged();
        }

        public List<info> getInfo(){
            return infoList;
        }

        public void toggleSelection(int position){

            selectView(position, !sparseBooleanArray.get(position));
            notifyDataSetChanged();
        }

        public void removeSelection(){
            sparseBooleanArray = new SparseBooleanArray();
            notifyDataSetChanged();
        }

        public void selectView(int position, boolean value){
            if(value){
                sparseBooleanArray.put(position, value);
            }else{
                sparseBooleanArray.delete(position);
            }
            notifyDataSetChanged();
        }

        public SparseBooleanArray getSelectedID(){
            return sparseBooleanArray;
        }
    }
}
