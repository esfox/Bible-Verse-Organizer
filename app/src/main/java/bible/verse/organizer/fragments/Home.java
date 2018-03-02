package bible.verse.organizer.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import bible.verse.organizer.MainActivity;
import bible.verse.organizer.adapters.VersesAdapter;
import bible.verse.organizer.organizer.R;
import bible.verse.organizer.utilities.DatabaseHandler;

public class Home extends Fragment
{
    public Home() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View layout = inflater.inflate(R.layout.fragment_home, container, false);

        setupToolbarAndDrawer(layout);

        setupList(layout);

        final FloatingActionButton newVerse = layout.findViewById(R.id.home_new_verse);
        newVerse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                newVerse();
            }
        });

        layout.findViewById(R.id.debug_show_database_contents)
                .setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ((MainActivity) getActivity()).showDatabaseContents();
            }
        });

        return layout;
    }

    private void setupToolbarAndDrawer(View layout)
    {
        final DrawerLayout drawerLayout = layout.findViewById(R.id.home_parent);

        Toolbar toolbar = layout.findViewById(R.id.home_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //FOR DEBUGGING ONLY - CLEAR DATABASE
        toolbar.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                ((MainActivity) getActivity()).d_clearDatabase();
                return true;
            }
        });

        final NavigationView drawer = layout.findViewById(R.id.home_navigation_drawer);
        drawer.setNavigationItemSelectedListener
            (new NavigationView.OnNavigationItemSelectedListener()
            {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item)
                {
                    drawerLayout.closeDrawer(GravityCompat.START);

                    switch(item.getItemId())
                    {
                        case R.id.navigation_drawer_home:
                            snack("Home");
                            break;

                        case R.id.navigation_drawer_favorites:
                            snack("Favorites");
                            break;

                        case R.id.navigation_drawer_categories:
                            categories();
                            break;

                        case R.id.navigation_drawer_tags:
                            snack("Tags");
                            break;

                        case R.id.navigation_drawer_settings:
                            snack("Settings");
                            break;

                        case R.id.navigation_drawer_about:
                            snack("About");
                            break;

                        case R.id.navigation_drawer_help:
                            snack("Help");
                            break;
                    }

                    return false;
                }
            });
    }

    private void setupList(View layout)
    {
        RecyclerView versesList = layout.findViewById(R.id.home_list);
        versesList.setHasFixedSize(true);
        versesList.setLayoutManager(new LinearLayoutManager(getContext()));

        VersesAdapter adapter = new VersesAdapter();
        versesList.setAdapter(adapter);
    }

    private void newVerse()
    {
        //Create class (static fields) for Fragment tags
        getActivity().getSupportFragmentManager()
            .beginTransaction()
            .setCustomAnimations
                (R.anim.slide_in_from_end, R.anim.slide_out_to_start,
                 R.anim.slide_in_from_start, R.anim.slide_out_to_end)
            .replace(R.id.parent_layout, new NewVerse(), FragmentTags.NEW_VERSE)
            .addToBackStack(FragmentTags.NEW_VERSE)
            .commit();
    }

    private void categories()
    {
        getActivity().getSupportFragmentManager()
        .beginTransaction()
        .setCustomAnimations
            (0, 0,
             R.anim.slide_in_from_end, R.anim.slide_out_to_start)
        .replace(R.id.parent_layout, new Categories(), FragmentTags.CATEGORIES)
        .addToBackStack(FragmentTags.CATEGORIES)
        .commit();
    }
    
    //TEMPORARY
    private void snack(String message)
    {
        Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }
}
