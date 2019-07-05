import React from 'react';

import CalendarDatePicker from "../calendar-date-picker";
import WarningPopup from "../warning-popup";


class SchedulesTable extends React.Component {

    constructor(props) {

        super(props);
        this.state = {
            isRemovingPopupVisible: false,
            idToRemove: null,
            isWarningPopupVisible: false
        };
    }

    cancelRemove = () => {
        this.setState({
            idToRemove: null,
            isRemovingPopupVisible: false
        });
    };

    setIdToRemove = (id) => {
        this.setState({
            idToRemove: id,
            isRemovingPopupVisible: true
        });
    };

    showWarning = () => {
        this.setState({
            isWarningPopupVisible: true
        });
    };

    closeWarning = () => {
        this.setState({
            isWarningPopupVisible: false
        });
    };

    getAllSchedules = () => {
        this.props.getAllSchedules();
    };

    removeSchedule = () => {

        const id = this.state.idToRemove;
        const schedulesUrl = '/schedules/';

        fetch(
            schedulesUrl + id,
            {
                credentials: 'include',
                mode: 'cors',
                method: 'DELETE',
                headers: {'Content-Type': 'application/json'}
            }
        )
            .then((response) => {
                if (response.status === 409) {
                    this.showWarning();
                }
            })
            .then(() => {
                this.setState(
                    {
                        idToRemove: null,
                        isRemovingPopupVisible: false
                    },
                    () => this.getAllSchedules()
                );
            })
            .catch((error) => {
                alert(`При удалении возникла ошибка: ${error}`);
            });
    };


    render() {

        const cancelRemove = this.cancelRemove;
        const removeSchedule = this.removeSchedule;

        const showRemovingPopup = this.state.isRemovingPopupVisible
            ? <WarningPopup>
                <div>
                    <div className="m-2">Вы действительно хотите удалить расписание?</div>
                    <ButtonCancel cancelRemove={cancelRemove}/>
                    <ButtonRemove removeSchedule={removeSchedule}/>
                </div>
            </WarningPopup>
            : '';

        const showWarningPopup = this.state.isWarningPopupVisible
            ? <WarningPopup>
                <div>
                    <div className="alert alert-danger m-2" role="alert">
                        Это расписание нельзя удалить, поскольку для него существуют записи на прием.
                    </div>
                    <button className="btn btn-primary" onClick={this.closeWarning}>
                        Понятно
                    </button>
                </div>
            </WarningPopup>
            : '';

        const schedules = this.props.schedules;
        const setIdToRemove = this.setIdToRemove;
        const handleDateChange = this.props.handleDateChange;
        const calendarDate = this.props.calendarDate;

        return (
            <div>
                <CalendarDatePicker date={calendarDate} handleDateChange={handleDateChange}/>

                <table className="table table-sm table-striped customTable">
                    <colgroup span="2" style={{width: 20 + '%'}}/>
                    <colgroup span="6" style={{width: 10 + '%'}}/>
                    <TableHeader/>
                    <TableBody date={calendarDate} schedules={schedules} setIdToRemove={setIdToRemove}/>
                </table>
                {showRemovingPopup}
                {showWarningPopup}
            </div>
        );
    }
}

const TableHeader = () => {
    return (
        <thead>
        <tr>
            <th>Специалист</th>
            <th>Услуга</th>
            <th>Кабинет</th>
            <th>Дата</th>
            <th>Начало приема</th>
            <th>Окончание приема</th>
            <th>Интервал приема</th>
            <th>Действие</th>
        </tr>
        </thead>
    );
};

const TableBody = (props) => {

    const rows = props.schedules.map((schedule) => {

        const scheduleDate = new Date();
        scheduleDate.setTime(Date.parse(schedule.date));

        return (
            <tr key={schedule.id}>
                <td>{schedule.specialistName}</td>
                <td>{schedule.servicesNames}</td>
                <td>{schedule.roomNumber}</td>
                <td>{scheduleDate.toLocaleDateString()}</td>
                <td>{schedule.startTime.substring(0, 5)}</td>
                <td>{schedule.endTime.substring(0, 5)}</td>
                <td>{schedule.interval + " минут"} </td>
                <td>
                    <button className="btn btn-danger" onClick={() => props.setIdToRemove(schedule.id)}>Удалить
                    </button>
                </td>
            </tr>
        )
    });

    return (
        <tbody>{rows}</tbody>
    );
};


const ButtonRemove = (props) => {
    return (
        <button className="btn btn-danger mx-auto"
                onClick={props.removeSchedule}>
            Да, удалить
        </button>
    );
};

const ButtonCancel = (props) => {
    return (
        <button className="btn btn-secondary m-4"
                onClick={props.cancelRemove}>
            Отменить
        </button>
    );
};

export default SchedulesTable;