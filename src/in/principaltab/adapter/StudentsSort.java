package in.principaltab.adapter;

import java.util.Comparator;

import in.principaltab.sqlite.Students;
import in.principaltab.sqlite.*;

public class StudentsSort implements Comparator<Students> {

	@Override
	public int compare(Students arg0, Students arg1) {		
		return arg0.getRollNoInClass() - arg1.getRollNoInClass();
	}

}
