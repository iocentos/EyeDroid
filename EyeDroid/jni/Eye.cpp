/*
 * Eye.cpp
 *
 *  Created on: Nov 2, 2014
 *      Author: centos
 */

#include "Eye.h"

namespace IMGP {


pthread_mutex_t* Eye::lock = new pthread_mutex_t();
Eye* Eye::instance = NULL;

void Eye::getInstance(Eye* copy){
	pthread_mutex_lock(lock);
		if( instance != NULL ){
			copy->setPupilFound(true);
			copy->setPupilDiamerer(instance->getPupilDiameter());
			copy->setPupil_X(instance->getPupil_X());
			copy->setPupil_Y(instance->getPupil_Y());
		}
	pthread_mutex_unlock(lock);
}

void Eye::newInstance(int x, int y , int diameter){

	pthread_mutex_lock(lock);
		if( instance == NULL ){
			instance = new Eye();
		}
		instance->setPupilFound(true);
		instance->setPupil_X(x);
		instance->setPupil_Y(y);
		instance->setPupilDiamerer(diameter);
	pthread_mutex_unlock(lock);
}

void Eye::nullInstance(){
	pthread_mutex_lock(lock);
		if( instance != NULL ){
			delete instance;
			instance = NULL;
		}
	pthread_mutex_unlock(lock);

}


Eye::Eye() {
	pupilFound = false;
	pupilDiameter = -1;
	pupil_X = -1;
	pupil_Y = -1;
}

bool Eye::isPupilFound() {
	return pupilFound;
}

void Eye::setPupilFound(bool found) {
	pupilFound = found;

	if (pupilFound == false) {
		pupilDiameter = -1;
		pupil_X = -1;
		pupil_Y = -1;
	}
}

int Eye::getPupilDiameter(){
	return pupilDiameter;
}

void Eye::setPupilDiamerer(int diameter){
	pupilDiameter = diameter;
}

int Eye::getPupil_X(){
	return pupil_X;
}

int Eye::getPupil_Y(){
	return pupil_Y;
}

void Eye::setPupil_X(int x){
	pupil_X = x;
}

void Eye::setPupil_Y(int y){
	pupil_Y = y;
}

}

