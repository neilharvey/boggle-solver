package io.neilharvey.bogglesolver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class WordListAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final Integer[] groups;
    private final HashMap<Integer, List<Word>> words;

    private WordListAdapter(Context context, Integer[] groups, HashMap<Integer, List<Word>> words) {
        this.context = context;
        this.groups = groups;
        this.words = words;
    }

    public static WordListAdapter create(Context context, Set<Word> words) {

        HashMap<Integer, List<Word>> groupedWords = new HashMap<>();

        for (Word word : words) {
            List<Word> list;
            int key = word.toString().length();

            if (groupedWords.containsKey(key)) {
                list = groupedWords.get(key);
            } else {
                list = new Vector<Word>();
                groupedWords.put(key, list);
            }

            list.add(word);
        }

        Integer[] groupArray = groupedWords.keySet().toArray(new Integer[groupedWords.keySet().size()]);
        Arrays.sort(groupArray);
        return new WordListAdapter(context, groupArray, groupedWords);
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        int key = groups[groupPosition];
        return words.get(key).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final Word childWord = (Word) getChild(groupPosition, childPosition);
        final String childText = childWord.toString().toUpperCase();

        if (convertView == null) {
            LayoutInflater inflater = getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        TextView listItemTextView = (TextView) convertView.findViewById(R.id.listItemTextView);
        listItemTextView.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int key = groups[groupPosition];
        return words.get(key).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        int length = groups[groupPosition];
        return length + " Letter Words";
    }

    @Override
    public int getGroupCount() {
        return groups.length;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_group, null);
        }

        TextView listGroupTextView = (TextView) convertView.findViewById(R.id.listGroupTextView);
        listGroupTextView.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private LayoutInflater getLayoutInflater() {
        return (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
}
