import React from 'react';

import AddScheduleForm from '../add-schedule-form';
import Header from '../header';
import Button from "../button";
import FormPopup from "../form-popup";
import SchedulesTable from "../schedules-table";
import Pagination from "../pagination";
import ServicesTable from "../services-table";
import AddServiceForm from "../add-service-form";
import SpecialistsTable from "../specialists-table";
import AddSpecialistForm from "../add-specialists-form";

import './app.css';


class App extends React.Component {

    constructor(props) {

        super(props);

        this.pageSize = 2;

        this.state = {
            schedules: [],
            isSchedulesLoaded: false,
            services: [],
            isServicesLoaded: false,
            specialists: [],
            isSpecialistsLoaded: false,
            activeServices: [],
            isActiveServicesLoaded: false,
            activeSpecialists: [],
            isActiveSpecialistsLoaded: false,
            organizationId: null,
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
        this.fetchAllServices();
        this.fetchAllSpecialists();
        this.fetchOrganization();
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
            .catch((error) => {
                console.error('Request failed', error);
            });
    };

    fetchOrganization = () => {

        const organizationUrl = '/organization';

        fetch(
            organizationUrl,
            {
                credentials: 'include',
                mode: 'cors'
            }
        )
            .then(response => response.json())
            .then((organization) => {

                this.setState({
                    organizationId: organization.id
                });
            })
            .catch((error) => {
                console.error('Request failed', error);
            });
    };

    fetchActiveServicesAndSpecialists = () => {

        Promise.all([this.fetchActiveServices(), this.fetchActiveSpecialists()])
            .then(values => {
                this.setState({
                    activeServices: values[0],
                    isActiveServicesLoaded: true,
                    activeSpecialists: values[1],
                    isActiveSpecialistsLoaded: true
                }, () => {
                    this.openPopup();
                });
            });
    };

    fetchActiveServices = () => {

        const servicesUrl = '/services/active';

        return fetch(
            servicesUrl,
            {
                credentials: 'include',
                mode: 'cors'
            }
        )
            .then(response => response.json())
            .catch((error) => {
                console.error('Request failed', error);
            });
    };

    fetchActiveSpecialists = () => {

        const specialistsUrl = '/specialists/active';

        return fetch(
            specialistsUrl,
            {
                credentials: 'include',
                mode: 'cors'
            }
        )
            .then(response => response.json())
            .catch((error) => {
                console.error('Request failed', error);
            });
    };


    fetchAllServices = () => {

        const servicesUrl = '/services/';

        fetch(servicesUrl, {
            credentials: 'include',
            mode: 'cors'
        })
            .then(response => response.json())
            .then((allServices) => {
                this.setState({
                    services: allServices,
                    isServicesLoaded: true
                });
            })
            .catch((error) => {
                console.error('Request failed', error);
            });
    };

    fetchAllSpecialists = () => {

        const specialistsUrl = '/specialists/';

        fetch(specialistsUrl, {
            credentials: 'include',
            mode: 'cors'
        })
            .then(response => response.json())
            .then((allSpecialists) => {
                this.setState({
                    specialists: allSpecialists,
                    isSpecialistsLoaded: true
                });
            })
            .catch((error) => {
                console.error('Request failed', error);
            });
    };


    render() {

        const {
            schedules,
            isSchedulesLoaded,
            services,
            isServicesLoaded,
            specialists,
            isSpecialistsLoaded,
            activeServices,
            isActiveServicesLoaded,
            activeSpecialists,
            isActiveSpecialistsLoaded,
            organizationId,
            isPopupVisible,
            calendarDate
        } = this.state;

        const addScheduleButtonOrPopup = isActiveServicesLoaded && isActiveSpecialistsLoaded && isPopupVisible
            ? <FormPopup>
                <AddScheduleForm handleShowForm={this.closePopup}
                                 calendarDate={calendarDate}
                                 getAllSchedules={this.fetchAllSchedules}
                                 services={activeServices}
                                 specialists={activeSpecialists}
                />
            </FormPopup>
            : <Button handleShowForm={this.fetchActiveServicesAndSpecialists}/>;

        const {isEmpty, totalPages, pageNumber} = this.state.page;

        const showPagination = (!isEmpty && totalPages > 1)
            ? <Pagination pageNumber={pageNumber} totalPages={totalPages} changePageNumber={this.changePageNumber}/>
            : '';

        const schedulesHeaderText = "Расписание записи на прием";
        const servicesHeader = "Справочник целей обращения";
        const specialistsHeader = "Справочник Специалист/Отдел";

        if (isSchedulesLoaded && isServicesLoaded && isSpecialistsLoaded && organizationId) {
            return (
                <div className="container">

                    <Header text={schedulesHeaderText}/>
                    <SchedulesTable
                        handleDateChange={this.handleDateChange}
                        calendarDate={calendarDate}
                        getAllSchedules={this.fetchAllSchedules}
                        schedules={schedules}
                    />
                    {showPagination}
                    {addScheduleButtonOrPopup}

                    <Header text={servicesHeader}/>
                    <ServicesTable
                        getAllServices={this.fetchAllServices}
                        services={services}
                    />
                    <AddServiceForm getAllServices={this.fetchAllServices} />

                    <Header text={specialistsHeader}/>
                    <SpecialistsTable
                        getAllSpecialists={this.fetchAllSpecialists}
                        specialists={specialists}
                    />
                    <AddSpecialistForm getAllSpecialists={this.fetchAllSpecialists} organizationId={organizationId}/>

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