package nl.yrck.mprog_to_dolist;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class TodoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        getSupportActionBar().setTitle(bundle.getString(TodoFragment.BUNDLE_LISTNAME));
        initFragment(bundle.getLong(TodoFragment.BUNDLE_LISTID));
    }

    private void initFragment(long listId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        TodoFragment todoFragment = TodoFragment.newInstance(listId);
        fragmentTransaction.add(R.id.fragment, todoFragment, TodoFragment.TAG);
        fragmentTransaction.commit();
    }

}
