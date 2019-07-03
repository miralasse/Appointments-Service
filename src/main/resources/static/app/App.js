import React from 'react';
import Form from './Form';
import Header from './Header';
import Button from "./Button";
import FormPopup from "./FormPopup";
import SchedulesTable from "./SchedulesTable";
import Pagination from "./Pagination";


class App extends React.Component {

    constructor(props) {

        super(props);

        this.pageSize = 2;

        this.state = {
            schedules: [],
            services: [],
            specialists: [],
            isSchedulesLoaded: false,
            isServicesLoaded: false,
            isSpecialistsLoaded: false,
            isPopupVisible: false,
            calendarDate: new Date(),
            page: {
                pageNumber: 0,
                size: this.pageSize
            }
        };
    }

    componentDidMount() {

        this.fetchAllSchedules();
        this.fetchServicesAndSpecialists();
    }

    handleDateChange = (date) => {
        this.setState({
                calendarDate: date,
                page: {
                    pageNumber: 0,
                    size: this.pageSize
                }
            },
            () => {
                this.fetchAllSchedules()
            }
        );
    };

    openPopup = () => {
        this.setState({
            isPopupVisible: true
        });
    };

    closePopup = () => {
        this.setState({
            isPopupVisible: false
        });
    };

    changePageNumber = (number) => {
        this.setState(previousState => ({
                page: {
                    ...previousState.page,
                    pageNumber: number
                }
            }),
            () => {
                this.fetchAllSchedules()
            }
        );
    };

    fetchAllSchedules = () => {
        const date = this.state.calendarDate.toISOString().split('T')[0];
        const schedulesUrl = '/schedules/?date=' + date + '&page=' + this.state.page.pageNumber + '&size=' + this.state.page.size;

        fetch(
            schedulesUrl,
            {
                credentials: 'include',
                mode: 'cors'
            }
        )
            .then(response => response.json())
            .then((page) => {

                const schedules = page.content.map(schedule => {

                    const specialistName = schedule.specialist.name;

                    const servicesNames = schedule.services
                        .map(service => service.name)
                        .join(', ');

                    return {
                        id: schedule.id,
                        specialistName: specialistName,
                        servicesNames: servicesNames,
                        roomNumber: schedule.roomNumber,
                        date: schedule.date,
                        startTime: schedule.startTime,
                        endTime: schedule.endTime,
                        interval: schedule.interval
                    }
                });

                this.setState(previousState => ({
                    page: {
                        ...previousState.page,
                        isEmpty: page.empty,
                        totalPages: page.totalPages,
                        pageNumber: page.number
                    },
                    schedules: schedules,
                    isSchedulesLoaded: true
                }));
            })
            .catch(function (error) {
                console.log('Request failed', error);
            });
    };

    fetchServicesAndSpecialists = () => {

        Promise.all([this.fetchAllServices(), this.fetchAllSpecialists()])
            .then(values => {
                this.setState({
                    services: values[0],
                    isServicesLoaded: true,
                    specialists: values[1],
                    isSpecialistsLoaded: true
                });
            });
    };

    fetchAllServices = () => {

        const servicesUrl = '/services/';

        return fetch(
            servicesUrl,
            {
                credentials: 'include',
                mode: 'cors'
            }
        )
            .then(response => response.json())
            .catch(function (error) {
                console.log('Request failed', error);
            });
    };

    fetchAllSpecialists = () => {

        const specialistsUrl = '/specialists/';

        return fetch(
            specialistsUrl,
            {
                credentials: 'include',
                mode: 'cors'
            }
        )
            .then(response => response.json())
            .catch(function (error) {
                console.log('Request failed', error);
            });
    };

    render() {

        const { services, specialists, schedules, isSchedulesLoaded, isServicesLoaded, isSpecialistsLoaded, isPopupVisible, calendarDate } = this.state;

        const buttonOrPopup = isPopupVisible
            ? <FormPopup>
                <Form handleShowForm={this.closePopup}
                      calendarDate={calendarDate}
                      getAllSchedules={this.fetchAllSchedules}
                      services={services}
                      specialists={specialists}
                />
            </FormPopup>
            : <Button handleShowForm={this.openPopup}/>;

        const {isEmpty, totalPages, pageNumber} = this.state.page;

        const showPagination = (!isEmpty && totalPages > 1)
            ? <Pagination pageNumber={pageNumber} totalPages={totalPages} changePageNumber={this.changePageNumber}/>
            : '';

        if (isSchedulesLoaded && isServicesLoaded && isSpecialistsLoaded) {
            return (
                <div className="container">
                    <Header/>
                    <SchedulesTable
                        handleDateChange={this.handleDateChange}
                        calendarDate={calendarDate}
                        getAllSchedules={this.fetchAllSchedules}
                        schedules={schedules}
                    />
                    {showPagination}
                    {buttonOrPopup}
                </div>
            );
        } else {
            return (
                <div className="spinner-border text-primary spinner" role="status">
                    <span className="sr-only">Loading...</span>
                </div>
            );
        }


    }
}

export default App;