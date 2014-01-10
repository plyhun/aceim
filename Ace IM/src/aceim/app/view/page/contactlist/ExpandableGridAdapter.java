package aceim.app.view.page.contactlist;

import java.util.List;

import aceim.api.dataentity.BuddyGroup;
import aceim.app.MainActivity;
import aceim.app.R;
import aceim.app.dataentity.ProtocolResources;
import aceim.app.view.page.contactlist.ContactListUpdater.ContactListModelGroup;
import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.GridView;

public class ExpandableGridAdapter extends ContactListAdapter {
	
	public ExpandableGridAdapter(List<ContactListModelGroup> groups, ProtocolResources resources) {
		super(groups, resources);
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		ContactListModelGroup g = getGroup(groupPosition);

		Resources res = parent.getContext().getResources();

		final int spacingDp = res.getDimensionPixelSize(R.dimen.contact_list_grid_items_spacing);
		final int colWidthDp = res.getDimensionPixelSize(R.dimen.contact_list_grid_item_size);
		final int rowHeightDp = res.getDimensionPixelSize(R.dimen.contact_list_grid_item_size);

		GridView view = getGroupContentView(convertView, parent, groupPosition, spacingDp, colWidthDp, g);		

		// calculate the column and row counts based on your display
		final int colCount = (int) Math.floor(parent.getWidth() / (colWidthDp + spacingDp));
		final int rowCount = (int) Math.ceil((g.getBuddyList().size() + 0d) / colCount);

		// calculate the height for the current grid
		final int gridHeightDp = Math.round(rowCount * (rowHeightDp + spacingDp));

		// set the height of the current grid
		view.getLayoutParams().height = gridHeightDp;

		return view;
	}

	@SuppressLint("InlinedApi")
	protected GridView getGroupContentView(View convertView, View parent, int groupPosition, int spacingDp, int colWidthDp, BuddyGroup g) {
		GridView view;
		if (convertView == null) {
			view = new GridView(parent.getContext());
			view.setVerticalSpacing(spacingDp);
			view.setHorizontalSpacing(spacingDp);
			view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			view.setStretchMode(GridView.STRETCH_SPACING_UNIFORM);
			view.setNumColumns(GridView.AUTO_FIT);
			view.setColumnWidth(colWidthDp);
			view.setPadding(spacingDp, spacingDp, spacingDp, spacingDp);
		} else {
			view = (GridView) convertView;
		}

		if (view.getTag() == null || view.getTag() != g) {
			view.setAdapter(new GroupItemsAdapter((MainActivity) parent.getContext(), R.layout.contact_list_grid_item, R.id.username, g.getBuddyList(), mResources, groupPosition));
			view.setTag(g);
		} else {
			((GroupItemsAdapter)view.getAdapter()).notifyDataSetChanged();
		}
		
		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}
	
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
	
	@Override
	public boolean hasStableIds() {
		return false;
	}
}
