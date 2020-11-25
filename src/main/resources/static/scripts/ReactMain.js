const { Icon } = window['MaterialUI'];

import TaskApp from "./TaskApp";
import ReportApp from "./ReportApp";


class Router extends React.Component {
	constructor(props){
		super(props);
		this.state = {
			currentApp: "tasks"	
		}
	}
	
	onCommonControlClick(appName){
		this.setState({
			currentApp: appName
		});
	}
	
	render(){
		var otherAppIcon = "";
		var currentApp = "";
		if(this.state.currentApp === "tasks"){
			otherAppIcon = <Icon onClick={() => {this.onCommonControlClick("report")}}>assessment</Icon>
			currentApp = <TaskApp/>
		} else {
			otherAppIcon = <Icon onClick={() => {this.onCommonControlClick("tasks")}}>assignment</Icon>
			currentApp = <ReportApp/>
		}
		return(
			<div>
				<div className="banner">
					<div className="appName"> 🤖 Ａｓｓｉｓｔａｎｔ</div>
					<div className="commonControls">
						<div>{otherAppIcon}</div>
						<div><Icon>settings</Icon></div>
					</div>
				</div>
				{currentApp}
			</div>
		);
	}
}

ReactDOM.render(<Router/>, document.getElementById('root'));