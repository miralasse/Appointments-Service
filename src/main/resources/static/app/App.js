import React from 'react';
import ScheduleTable from './ScheduleTable';
import Form from './Form';
import Header from './Header';
import Button from "./Button";
import Popup from "./Popup";


class App extends React.Component {

    constructor(props) {

        super(props);
        this.state = {
            schedules: [
                {
                    id: 1001,
                    specialist: "Специалист 1",
                    service: "Получение путевки в ДОО",
                    room: "26",
                    date: new Date(),
                    startTime: new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate(), 8, 0, 0, 0),
                    endTime: new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate(), 14, 0, 0, 0),
                    interval: "15"
                },
                {
                    id: 1002,
                    specialist: "Специалист 2",
                    service: "Постановка на очередь в ДС",
                    room: "34",
                    date: new Date(),
                    startTime: new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate(), 9, 0, 0, 0),
                    endTime: new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate(), 16, 0, 0, 0),
                    interval: "15"
                }
            ],
            isPopupVisible: false,
            calendarDate: new Date()
        };

        this.handleDateChange = this.handleDateChange.bind(this);
        this.removeSchedule = this.removeSchedule.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.openPopup = this.openPopup.bind(this);
        this.closePopup = this.closePopup.bind(this);
    }

    handleDateChange(date) {
        this.setState({
            calendarDate: date
        });
    }

    removeSchedule(id) {
        this.setState(state => {
            state.schedules.filter(schedule => schedule.id !== id)
        });
    }


    handleSubmit(schedule) {
        this.setState({
            schedules: [...this.state.schedules, schedule]
        });
    }

    openPopup() {
        this.setState({
            isPopupVisible: true
        });
    }

    closePopup() {
        this.setState({
            isPopupVisible: false
        });
    }

    render() {

        const {schedules, isPopupVisible} = this.state;
        const buttonOrPopup = isPopupVisible
            ? <Popup>
                <Form handleShowForm={this.closePopup}
                      handleSubmit={this.handleSubmit}
                      calendarDate={this.state.calendarDate}
                />
            </Popup>
            : <Button handleShowForm={this.openPopup}/>;

        return (
            <div className="container">
                <Header/>
                <ScheduleTable schedules={schedules}
                               removeSchedule={this.removeSchedule}
                               handleDateChange={this.handleDateChange}
                               calendarDate={this.state.calendarDate}
                />
                {buttonOrPopup}
            </div>
        );
    }
}

export default App