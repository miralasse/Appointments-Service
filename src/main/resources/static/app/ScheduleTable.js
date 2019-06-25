import React from 'react'
import CalendarDatePicker from "./CalendarDatePicker";


class ScheduleTable extends React.Component {


    render() {

        const schedules = this.props.schedules;

        const removeSchedule = this.props.removeSchedule;
        const handleDateChange = this.props.handleDateChange;
        const calendarDate = this.props.calendarDate;

        return (
            <div>
                <CalendarDatePicker date={calendarDate} handleDateChange={handleDateChange}/>

                <table className="table table-sm table-striped scheduleTable">
                    <colgroup span="2" style={{width: 20 + '%'}}/>
                    <colgroup span="6" style={{width: 10 + '%'}}/>
                    <TableHeader/>
                    <TableBody date={calendarDate} schedules={schedules} removeSchedule={removeSchedule}/>
                </table>
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

    const chosenDate = props.date;
    const filteredSchedules = props.schedules.filter(

        (schedule) => {
            const scheduleDateTime = new Date();
            scheduleDateTime.setTime(Date.parse(schedule.date));
            const scheduleDate = new Date(scheduleDateTime.getFullYear(), scheduleDateTime.getMonth(), scheduleDateTime.getDate());

            const filterDateTime = new Date();
            filterDateTime.setTime(Date.parse(chosenDate));
            const filterDate = new Date(filterDateTime.getFullYear(), filterDateTime.getMonth(), filterDateTime.getDate());

            return scheduleDate.getTime() === filterDate.getTime();
        });

    const rows = filteredSchedules.map(
        (schedule) => {
            return (
                <tr key={schedule.id}>
                    <td>{schedule.specialist}</td>
                    <td>{schedule.service}</td>
                    <td>{schedule.room}</td>
                    <td>{schedule.date.toLocaleDateString()}</td>
                    <td>{schedule.startTime.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit', hour12: false})}</td>
                    <td>{schedule.endTime.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit', hour12: false})}</td>
                    <td>{schedule.interval + " минут"} </td>
                    <td>
                        <button className="btn btn-warning" onClick={() => props.removeSchedule(schedule.id)}>Удалить</button>
                    </td>
                </tr>
            )
        });

    return (
        <tbody>{rows}</tbody>
    );
};


export default ScheduleTable