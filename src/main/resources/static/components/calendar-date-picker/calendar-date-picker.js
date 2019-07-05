import React from 'react';

import DatePicker, {registerLocale} from "react-datepicker";
import ru from "date-fns/locale/ru";

registerLocale("ru", ru);

class CalendarDatePicker extends React.Component {

    render() {
        return (
            <div className="form-group row">
                <div className="col-sm-3 customDatePickerWidth">
                    <DatePicker locale="ru"
                                selected={this.props.date}
                                onChange={this.props.handleDateChange}
                                dateFormat="d MMMM yyyy"
                                className="form-control"
                    />
                </div>
            </div>
        );
    }

}

export default CalendarDatePicker;