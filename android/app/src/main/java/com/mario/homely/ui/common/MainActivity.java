package com.mario.homely.ui.common;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mario.homely.R;
import com.mario.homely.ui.properties.PropertiesMapFragment;
import com.mario.homely.ui.properties.listview.PropertiesListFragment;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private BottomAppBar bottomAppBar;
    FragmentTransaction fragmentChanger;
    private Fragment propertiesMapFragment, propertiesListFragment, profileFragment, addPropertyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpBottomAppBar();
        propertiesMapFragment = new PropertiesMapFragment();
        fragmentChanger = getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, propertiesMapFragment);
        fragmentChanger.commit();
        //click event over FAB
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "FAB Clicked.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpBottomAppBar() {
        //find id
        bottomAppBar = findViewById(R.id.bar);

        //set bottom bar to Action bar as it is similar like Toolbar
        setSupportActionBar(bottomAppBar);

        //click event over Bottom bar menu item
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.btn_list_view:
                        Toast.makeText(MainActivity.this, "List View clicked.", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });

        //click event over navigation menu like back arrow or hamburger icon
        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open bottom sheet
                BottomSheetDialogFragment bottomSheetDialogFragment = BottomSheetNavigationFragment.newInstance();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
            }
        });
    }

    //Inflate menu to bottom bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_list_view:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void btnListClick() {
//        Objects.requireNonNull(getView()).findViewById(R.id.btn_list_view).setOnClickListener(view -> {
//            Objects.requireNonNull(getFragmentManager()).beginTransaction()
//                    .replace(R.id.contenedor, new PropertiesListFragment())
//                    .commit();
//        });
    }
}