\# Study Tracker - Java Swing Application



A simple yet effective desktop application for logging and managing study sessions, built entirely with Java Swing.



This desktop study-tracking application allows users to log, edit, and delete study sessions, with all data saved locally to a CSV file. The interface includes a sortable, filterable log table and a summary of total study hours by subject. It's a straightforward tool designed to help students track their learning progress and build consistent study habits.



\## Features



\- \*\*Log Study Sessions\*\*: Add new entries with date, subject, duration, and a description.

\- \*\*Full CRUD Functionality\*\*: Create, Read, Update, and Delete any study log.

\- \*\*Persistent Storage\*\*: All data is automatically saved to a local `StudyTracker.csv` file, so your progress is never lost.

\- \*\*Sortable \& Filterable Table\*\*: Easily view your logs and click column headers to sort them. Use the search bar to filter entries in real-time.

\- \*\*Study Summary\*\*: Generate a quick summary to see the total hours you've dedicated to each subject.

\- \*\*Modern UI\*\*: A clean and user-friendly interface built with Java's Nimbus Look and Feel.



\## Technologies Used



\- \*\*Language\*\*: Java

\- \*\*UI Framework\*\*: Java Swing



\## Getting Started



Follow these instructions to compile and run the project on your local machine.



\### Prerequisites



\- You must have a \*\*Java Development Kit (JDK)\*\* installed on your system (version 8 or newer is recommended).



\### Running the Application



1\.  \*\*Clone or download\*\* this repository to your local machine.

2\.  \*\*Open a terminal\*\* or command prompt.

3\.  \*\*Navigate\*\* to the directory where you saved the `.java` files:

&nbsp;   ```sh

&nbsp;   cd path/to/your/StudyTrackerProject

&nbsp;   ```

4\.  \*\*Compile\*\* all the Java source files:

&nbsp;   ```sh

&nbsp;   javac \*.java

&nbsp;   ```

5\.  \*\*Run\*\* the main GUI class:

&nbsp;   ```sh

&nbsp;   java StudyTrackerGUI

&nbsp;   ```

The application window should now appear.



\## File Structure



\- `StudyTrackerGUI.java`: The main class that builds and manages the graphical user interface (GUI) and handles all user interactions.

\- `StudyTracker.java`: The backend logic class that manages the list of study logs and handles all file I/O (saving to and loading from the CSV file).

\- `StudyLog.java`: The data model class. A simple Java object that represents a single study log entry.



\## License



This project is licensed under the MIT License - see the \[LICENSE.md](LICENSE.md) file for details.

