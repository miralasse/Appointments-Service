import React from 'react';

import DatePicker, {registerLocale} from "react-datepicker";
import ru from "date-fns/locale/ru";

import './add-schedule-form.css';

registerLocale("ru", ru);

class AddScheduleForm extends React.Component {

    constructor(props) {

        super(props);
        this.initialState = {
            id: null,
            specialist: {},
            roomNumber: '',
            date: props.calendarDate,
            services: [],
            startTime: '',
            endTime: '',
            interval: "15"
        };
        this.state = this.initialState;
    }


    handleChangeSpecialist = (event) => {
        this.setState({
            specialist: {
                id: event.target.value,
                name: [...event.target.options]
                    .filter(option => option.selected)
                    .map(option => option.label)
                    .join()
            }
        });
    };

    handleChangeServices = (event) => {
        this.setState({
            services: [...event.target.options]
                .filter(option => option.selected)
                .map(option => {
                    return {
                        id: option.value,
                        name: option.label
                    };
                })
        });
    };

    handleChangeRoom = (event) => {
        this.setState({
            roomNumber: event.target.value
        });
    };


    handleChangeStartTime = (time) => {
        this.setState({
            startTime: time
        });
    };

    handleChangeEndTime = (time) => {
        this.setState({
            endTime: time
        });
    };

    handleChangeInterval = (event) => {
        this.setState({
            interval: event.target.value
        });
    };

    formatDate = (date) => {
        return JSON.stringify(date).substring(1, 11);
    };

    formatTime = (time) => {
        return time.toLocaleString().substring(12, 20);
    };

    submitForm = () => {

        const {date, startTime, endTime} = this.state;

        const schedule = {
            ...this.state,
            date: this.formatDate(date),
            startTime: this.formatTime(startTime),
            endTime: this.formatTime(endTime)
        };

        this.addSchedule(schedule);
        this.setState(this.initialState);
        this.handleShowForm();
    };

    addSchedule = (schedule) => {

        const schedulesUrl = '/schedules/';
        fetch(
            schedulesUrl,
            {
                credentials: 'include',
                mode: 'cors',
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(schedule)
            }
        )
            .then(() => {
                this.getAllSchedules();
            })
            .catch((error) => {
                alert(`При добавлении возникла ошибка: ${error}`);
            });
    };

    handleShowForm = () => {
        this.props.handleShowForm();
    };

    getAllSchedules = () => {
        this.props.getAllSchedules();
    };

    render() {

        const timeIntervalForTimePicker = 15;
        const timeFormatForTimePicker = "HH:mm";
        const timeCaptionForTimePicker = "Время";
        const localeForTimePicker = "ru";

        const activeServices = this.props.services;
        const activeSpecialists = this.props.specialists;

        const {specialist, roomNumber, date, services, startTime, endTime, interval} = this.state;

        const specialistId = specialist.id;
        const servicesIds = services.map(serviceDTO => serviceDTO.id);

        const isEnabled = specialistId && roomNumber && date && servicesIds.length && startTime && endTime && interval;

        return (

            <div>
                <h3>Добавление расписания</h3>
                <form>
                    <div className="form-group row">
                        <label htmlFor="selectSpecialist" className="col-sm-3 col-form-label">Специалист</label>
                        <div className="col-sm-9">
                            <select value={specialistId}
                                    onChange={this.handleChangeSpecialist}
                                    className="form-control"
                                    id="selectSpecialist">
                                <option value="">
                                    --Выбрать специалиста--
                                </option>
                                {activeSpecialists.map(specialist => (
                                    <option key={specialist.id}
                                            value={specialist.id}
                                            label={specialist.name}
                                    />
                                ))}
                            </select>
                        </div>
                    </div>

                    <div className="form-group row">
                        <label htmlFor="selectServices" className="col-sm-3 col-form-label">Услуга</label>
                        <div className="col-sm-9">
                            <select multiple={true}
                                    value={servicesIds}
                                    onChange={this.handleChangeServices}
                                    className="form-control"
                                    id="selectServices">
                                {activeServices.map(service => (
                                    <option key={service.id}
                                            value={service.id}
                                            label={service.name}
                                    />
                                ))}
                            </select>
                        </div>
                    </div>

                    <div className="form-group row">
                        <label htmlFor="inputRoom" className="col-sm-3 col-form-label">Кабинет</label>
                        <div className="col-sm-3">
                            <input type="text"
                                   value={roomNumber}
                                   onChange={this.handleChangeRoom}
                                   className="form-control"
                                   id="inputRoom"
                            />
                        </div>
                    </div>

                    <div className="form-group row">
                        <label htmlFor="inputStartTime" className="col-sm-3 col-form-label">Время начала приема</label>
                        <div className="col-sm-3 customDatePickerWidth">
                            <DatePicker
                                locale={localeForTimePicker}
                                selected={startTime}
                                selectsStart
                                startDate={startTime}
                                endDate={endTime}
                                onChange={this.handleChangeStartTime}
                                showTimeSelect
                                showTimeSelectOnly
                                timeIntervals={timeIntervalForTimePicker}
                                dateFormat={timeFormatForTimePicker}
                                timeFormat={timeFormatForTimePicker}
                                timeCaption={timeCaptionForTimePicker}
                                className="form-control"
                                id="inputStartTime"
                            />
                        </div>
                    </div>

                    <div className="form-group row">
                        <label htmlFor="inputEndTime" className="col-sm-3 col-form-label">Время окончания приема</label>
                        <div className="col-sm-3 customDatePickerWidth">
                            <DatePicker
                                locale={localeForTimePicker}
                                selected={endTime}
                                selectsEnd
                                startDate={startTime}
                                endDate={endTime}
                                onChange={this.handleChangeEndTime}
                                minDate={startTime}
                                showTimeSelect
                                showTimeSelectOnly
                                timeIntervals={timeIntervalForTimePicker}
                                dateFormat={timeFormatForTimePicker}
                                timeFormat={timeFormatForTimePicker}
                                timeCaption={timeCaptionForTimePicker}
                                className="form-control"
                                id="inputEndTime"
                            />
                        </div>
                    </div>

                    <div className="form-group row">
                        <label htmlFor="inputInterval" className="col-sm-3 col-form-label">Интервал приема в
                            минутах</label>
                        <div className="col-sm-3">
                            <input type="text"
                                   value={interval}
                                   onChange={this.handleChangeInterval}
                                   className="form-control" id="inputInterval"
                            />
                        </div>
                    </div>

                    <div className="form-row align-items-center">
                        <div className="col-auto my-1">
                            <input type="button"
                                   value="Сохранить"
                                   onClick={this.submitForm}
                                   className="btn btn-success"
                                   disabled={!isEnabled}/>
                        </div>

                        <div className="col-auto my-1">
                            <button type="button"
                                    className="btn btn-secondary"
                                    onClick={this.handleShowForm}>
                                Отменить
                            </button>
                        </div>
                    </div>
                </form>
                <button className="btn btn-outline-secondary btn-sm popup-closer" onClick={this.handleShowForm}>
                    Закрыть
                </button>
            </div>
        );
    }
}

export default AddScheduleForm;