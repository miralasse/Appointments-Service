import React from 'react';
import ReactDOM from 'react-dom';

import './form-popup.css';


export class FormPopup extends React.Component {

    constructor(props) {

        super(props);
        this.state = {
            popup: false
        };
    }

    render() {
        return (<noscript></noscript>);
    }


    componentDidMount() {
        this.renderPopup();
    }

    componentDidUpdate() {
        this.renderPopup();
    }

    componentWillUnmount() {
        ReactDOM.unmountComponentAtNode(this.popup);
        document.body.removeChild(this.popup);
    }

    renderPopup() {
        if (!this.popup) {
            this.popup = document.createElement("div");
            document.body.appendChild(this.popup);
        }

        ReactDOM.render(
            <div className="popup-overlay">
                <div className="popup-content">
                    {this.props.children}
                </div>
            </div>,
            this.popup
        );
    }
}

export default FormPopup;