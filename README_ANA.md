# Calender Functions

To Optimize the functionalities it was decided to implememt to Types of 
Calender Tools in the App.

the Choocen options are: 
* A Visual Calender Fragment
* iCal 
## For Save Cleaning!
- ./gradlew clean

- rm -rf build app/build

Otherwise the project will break because of old Android

### Reasons and Functions:



#### CalenderFragment
- Local Function in Android
- Allows to see: todos, Days, Months, year, separately.


#### iCal
- Allows to export all the Todos into an external Calender like: google Calender, Outlook, etc.
- Allows to import externals events --> not sure to implement yet

#### Pros 
- Both, separately and togheter allowed to integrate with AlarmManager, Services and Notifications

#### Cons
- No Cons. Both compatible with all the already integrated functions 

## CalenderFragment Architecture
#### New Fragment and Layout
- CalenderFragment.java 
First with Linear Layout
- CalenderViewModel.java
#### res/layout
- fragment_calender.xml
#### navigation (optional)
- nav_graph.xml (with Navigation Component??)
#### Dependencies
- implementation 'com.github.prolificinteractive:material-calendarview:2.0.1' 
--> modify to CompactCalenderView, due to Hardware Restrictions
#### layout
- monthly Calender
- ~~RecyclerView~~ ~~for~~ Todos on determined Date
#### Fragment
- Listen the selected Date
- Ask Room to obtain the Todos for a specific date
- Update the ~~RecyclerView~~ View
#### Calender ViewModel??
#### Dao Extention
#### Dekos - MaterialCalenderView
- colors with priority
- point on Date with ToDo
#### Calender Button on Menu


