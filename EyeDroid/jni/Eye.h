/*
 * Eye.h
 */

#include <pthread.h>
#ifndef EYE_H_
#define EYE_H_

namespace IMGP{

/*
 * Eye class to hold all the information about the found pupil.
 * It implements the singleton pattern and the instance is guarded
 * by a lock since the object can be accessed by two threads.
 */
class Eye {
private:
	bool pupilFound;

	int pupilDiameter;

	int pupil_X;
	int pupil_Y;

	static Eye* instance;

	static pthread_mutex_t* lock;

	void setPupilFound(bool);

	void setPupil_Y(int);
	void setPupil_X(int);
	void setPupilDiamerer(int);
protected:

public:

	static void getInstance(Eye*);

	static void newInstance(int , int , int);

	static void nullInstance();


	Eye();

	bool isPupilFound();


	int getPupilDiameter();


	int getPupil_X();


	int getPupil_Y();


}; /*end of class Eye.h*/

}
#endif /* EYE_H_ */
