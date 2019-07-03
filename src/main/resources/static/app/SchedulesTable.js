import React from 'react';
import CalendarDatePicker from "./CalendarDatePicker";
import RemovingPopup from "./RemovingPopup";


class SchedulesTable extends React.Component {

    constructor(props) {

        super(props);
        this.state = {
            isRemovingPopupVisible: false,
            idToRemove: null
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
            .then(() => {
                this.setState({
                        idToRemove: null,
                        isRemovingPopupVisible: false
                    }, () => {
                        this.getAllSchedules();
                    }
                );
            })
            .catch(function (error) {
                console.log('Request failed', error);
            });
    };


    render() {

        const cancelRemove = this.cancelRemove;
        const removeSchedule = this.removeSchedule;

        const showRemovingPopup = this.state.isRemovingPopupVisible
            ? <RemovingPopup>
                <div>
                    <div className="m-2">Вы действительно хотите удалить расписание?</div>
                    <ButtonCancel cancelRemove={cancelRemove}/>
                    <ButtonRemove removeSchedule={removeSchedule}/>
                </div>
            </RemovingPopup>
            : '';

        const schedules = this.props.schedules;
        const setIdToRemove = this.setIdToRemove;
        const handleDateChange = this.props.handleDateChange;
        const calendarDate = this.props.calendarDate;

        return (
            <div>
                <CalendarDatePicker date={calendarDate} handleDateChange={handleDateChange}/>

                <table className="table table-sm table-striped scheduleTable">
                    <colgroup span="2" style={{width: 20 + '%'}}/>
                    <colgroup span="6" style={{width: 10 + '%'}}/>
                    <TableHeader/>
                    <TableBody date={calendarDate} schedules={schedules} setIdToRemove={setIdToRemove}/>
                </table>
                {showRemovingPopup}
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
                    <button className="btn btn-warning" onClick={() => props.setIdToRemove(schedule.id)}>Удалить
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