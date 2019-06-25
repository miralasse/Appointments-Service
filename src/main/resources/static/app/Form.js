import React from 'react';
import 'bootstrap/dist/css/bootstrap.css';
import DatePicker, {registerLocale} from "react-datepicker";
import ru from "date-fns/locale/ru";

registerLocale("ru", ru);

class Form extends React.Component {

    constructor(props) {

        super(props);
        this.initialState = {
            id: '',
            specialist: "Специалист 1",
            service: "Получение путевки в ДОО",
            room: '',
            date: props.calendarDate,
            startTime: '',
            endTime: '',
            interval: "15"
        };

        this.state = this.initialState;
        this.submitForm = this.submitForm.bind(this);
        this.handleShowForm = this.handleShowForm.bind(this);
        this.handleChangeSpecialist = this.handleChangeSpecialist.bind(this);
        this.handleChangeService = this.handleChangeService.bind(this);
        this.handleChangeRoom = this.handleChangeRoom.bind(this);
        this.handleChangeStartTime = this.handleChangeStartTime.bind(this);
        this.handleChangeEndTime = this.handleChangeEndTime.bind(this);
        this.handleChangeInterval = this.handleChangeInterval.bind(this);
    }


    handleChangeSpecialist(event) {
        this.setState({
            specialist: event.target.value
        });
    }

    handleChangeService(event) {
        this.setState({
            service: event.target.value
        });
    }

    handleChangeRoom(event) {
        this.setState({
            room: event.target.value
        });
    }


    handleChangeStartTime(time) {
        this.setState({
            startTime: time
        });
    }

    handleChangeEndTime(time) {
        this.setState({
            endTime: time
        });
    }

    handleChangeInterval(event) {
        this.setState({
            interval: event.target.value
        });
    }


    submitForm() {

        const schedule = {
            ...this.state,
            id: Math.floor(Math.random() * 1000 + 1)
        };

        this.props.handleSubmit(schedule);
        this.setState(this.initialState);
        this.handleShowForm();
    }

    handleShowForm() {
        this.props.handleShowForm();
    }

    render() {

        const timeIntervalForTimePicker = 15;
        const timeFormatForTimePicker = "HH:mm";
        const timeCaptionForTimePicker = "Время";
        const localeForTimePicker = "ru";

        const {specialist, service, room, startTime, endTime, interval} = this.state;

        const isEnabled = startTime && endTime;

        return (

            <div>
                <h3>Добавление расписания</h3>
                <form>
                    <div className="form-group row">
                        <label htmlFor="selectSpecialist" className="col-sm-3 col-form-label">Специалист</label>
                        <div className="col-sm-9">
                            <select value={specialist}
                                    onChange={this.handleChangeSpecialist}
                                    className="form-control"
                                    id="selectSpecialist">
                                <option value="Специалист 1">Специалист 1</option>
                                <option value="Специалист 2">Специалист 2</option>
                                <option value="Специалист 3">Специалист 3</option>
                            </select>
                        </div>
                    </div>

                    <div className="form-group row">

                        <label htmlFor="selectService" className="col-sm-3 col-form-label">Услуга</label>
                        <div className="col-sm-9">
                            <select value={service}
                                    onChange={this.handleChangeService}
                                    className="form-control"
                                    id="selectService">
                                <option value="Получение путевки в ДОО">Получение путевки в ДОО</option>
                                <option value="Постановка на очередь в ДС">Постановка на очередь в ДС</option>
                                <option value="Льготное питание">Льготное питание</option>
                            </select>
                        </div>
                    </div>

                    <div className="form-group row">
                        <label htmlFor="inputRoom" className="col-sm-3 col-form-label">Кабинет</label>
                        <div className="col-sm-3">
                            <input type="text"
                                   value={room}
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
                            <button className="btn btn-secondary"
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

export default Form