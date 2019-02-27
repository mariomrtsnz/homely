package com.mario.homely.ui.common.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mario.homely.R;
import com.mario.homely.responses.PropertyResponse;
import com.mario.homely.ui.properties.PropertiesMapFragment;
import com.mario.homely.ui.properties.addOne.AddOnePropertyFragment;
import com.mario.homely.ui.properties.addOne.AddPropertyListener;
import com.mario.homely.ui.properties.detail.PropertyDetailsActivity;
import com.mario.homely.ui.properties.listview.PropertiesListFragment;
import com.mario.homely.ui.properties.listview.PropertiesListListener;
import com.mario.homely.ui.user.MyProfileListener;
import com.mario.homely.util.UtilToken;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements PropertiesListListener, MyProfileListener, AddPropertyListener {

    private BottomAppBar bottomAppBar;
    private boolean isInMap = true;
    FloatingActionButton fabMap;
    FragmentTransaction fragmentChanger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpBottomAppBar();
        fragmentChanger = getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new PropertiesListFragment());
        fragmentChanger.commit();
        fabMap = findViewById(R.id.fab_map);
        fabMap.setOnClickListener(view -> {
            if (isInMap) {
                Objects.requireNonNull(getSupportFragmentManager()).beginTransaction()
                        .replace(R.id.contenedor, new PropertiesMapFragment())
                        .commit();
//                fabMap.animate().setDuration(500).rotationY(180);
                fabMap.setImageResource(R.drawable.ic_view_list_white_24dp);
                isInMap = !isInMap;
            } else {
                Objects.requireNonNull(getSupportFragmentManager()).beginTransaction()
                        .replace(R.id.contenedor, new PropertiesListFragment())
                        .commit();
//                fabMap.animate().setDuration(500).rotationY(0);
                fabMap.setImageResource(R.drawable.ic_map_white_24dp);
                isInMap = !isInMap;
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
                    case R.id.btn_add:
                        Objects.requireNonNull(getSupportFragmentManager()).beginTransaction()
                                .replace(R.id.contenedor, new AddOnePropertyFragment())
                                .commit();
                        return true;
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
        // TODO: Comprobar que si el token está nulo no salga la opción de añadir una propiedad.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onPropertyClick(View v, PropertyResponse property) {
        Intent i = new Intent(this, PropertyDetailsActivity.class);
        i.putExtra("propertyId", property.getId());
        startActivity(i);
    }

    @Override
    public void onAddSubmit() {

    }
}